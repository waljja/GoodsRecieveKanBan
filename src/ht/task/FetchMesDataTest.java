package ht.task;

import ht.util.ConAegis;
import ht.util.ConMes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 定时任务，获取数据
 * <p>
 * 测试 MES 2.0 收货看板
 *
 * @author 丁国钊
 * @date 2022-11-18
 */
public class FetchMesDataTest {
    private static Log commonsLog = LogFactory.getLog(AutoUrgentMaterialCheckOCR.class);

    /**
     * 获取 Aegis 库位信息
     * <p>
     * 插入UID_xTend_MaterialTransactionsRHDCDW, ItemInventoryHistories表
     * <p>
     */
    public void start() {
        try {
            ConAegis aegis = new ConAegis();
            ConMes mes = new ConMes();
            commonsLog.info("启动-----");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            // 今天
            String currentDate = simpleDateFormat.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, -2);
            // 前天
            String dateSub2 = simpleDateFormat.format(cal.getTime());
            commonsLog.info("c"+currentDate);
            commonsLog.info("d"+dateSub2);
            commonsLog.info("库存表插入");
            // 执行 SQL 开始时间
            long startTime = System.currentTimeMillis();
            /*
             * 获取 aegis 字段
            */
            ResultSet rs;
            rs = aegis.executeQuery("if object_id(N'tempdb..#t1',N'U') is not null" +
                    "DROP Table #t1" +
                    "select rid,UpAegisDATE as UpAegisDATE into #t1 from (select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103, UpAegisDATE from [172.31.2.26].[imslabel].[dbo].vendorrid" +
                    "where convert(varchar(10),GRNDATE,23) between '" + dateSub2 + "' and '" + currentDate + "' and printQTY<>0.0 and plent in ('1100','5000')" +
                    "union  select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103, UpAegisDATE from [172.31.2.26].[imslabel].[dbo].pcbvendorrid" +
                    "where convert(varchar(10),GRNDATE,23) between '" + dateSub2 + "' and '" + currentDate + "' and printQTY<>0.0 and plent in ('1100','5000') )m  order by grn, partNumber" +
                    "select " +
                    "    II.Identifier as UID, FRB.Name as ToStock_Input,c.UpAegisDATE as TransactionTime" +
                    "from" +
                    "    [HT_FactoryLogix].[dbo].ItemInventories II" +
                    "    left join [HT_FactoryLogix].[dbo].FactoryResourceBases FRB" +
                    "        on FRB.ID = II.StockResourceID" +
                    "    inner join #t1  c " +
                    "        on ii.Identifier collate Chinese_PRC_CI_AS = c.rid");
            // 插入 131 UID 表作为测试数据
            while (rs.next()) {
                String uid = rs.getString("UID");
                // 库位
                String toStockInput = rs.getString("ToStock_Input");
                Date transactionTime = rs.getTimestamp("TransactionTime");
                Boolean isInsert = mes.executeUpdate("IF NOT EXISTS" +
                        "(" +
                        "   select * " +
                        "   from " +
                        "      UID_xTend_MaterialTransactionsRHDCDW " +
                        "   where " +
                        "      UID = '" + uid + "'" +
                        ")" +
                        "   insert into [dbo].[UID_xTend_MaterialTransactionsRHDCDW] " +
                        "      ([TransactionHistoryId],[UID],[Quantity],[ToStock_Input],[TransactionUser],[TransactionTime],[Plant]) " +
                        "   values " +
                        "      (NEWID(),'" + uid + "','100','" + toStockInput + "','ding','" + transactionTime + "','1100')" +
                        "else " +
                        "   update " +
                        "       UID_xTend_MaterialTransactionsRHDCDW " +
                        "   set " +
                        "       ToStock_Input = '" + toStockInput + "',TransactionTime = '" + transactionTime + "'" +
                        "   where" +
                        "       UID = '" + uid + "'");
                if (isInsert) {
                    commonsLog.info("成功");
                } else {
                    commonsLog.info("失败");
                }
                commonsLog.info("UID: " + uid + "    ToStock_Input: " + toStockInput + "    TransactionTime: " + transactionTime);
            }
            commonsLog.info("历史表插入");
            /*
             * 查历史表数据
             */
            ResultSet rsHistory;
            rsHistory = aegis.executeQuery("if object_id(N'tempdb..#t2',N'U') is not null " +
                    "DROP Table #t2; " +
                    "select rid into #t2 from (select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103, UpAegisDATE from [172.31.2.26].[imslabel].[dbo].vendorrid " +
                    "where convert(varchar(10),GRNDATE,23) between '" + dateSub2 + "' and '" + currentDate + "' and printQTY<>0.0 and plent in ('1100','5000') " +
                    "union  select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103, UpAegisDATE from [172.31.2.26].[imslabel].[dbo].pcbvendorrid " +
                    "where convert(varchar(10),GRNDATE,23) between '" + dateSub2 + "' and '" + currentDate + "' and printQTY<>0.0 and plent in ('1100','5000') )m  order by grn, partNumber; " +
                    "select " +
                    "   II.Identifier as UID,SL.Identifier as historyStock,DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS localtime " +
                    "from " +
                    "   [HT_FactoryLogix].[dbo].[ItemInventories] II " +
                    "   left join [HT_FactoryLogix].dbo.ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
                    "   left join [HT_FactoryLogix].dbo.StockLocations SL on IIH.StockLocationID = SL.ID " +
                    "where " +
                    "   II.Identifier collate Chinese_PRC_CI_AS in(select distinct rid from #t2)");
            // 插入 131 移库历史表
            while (rsHistory.next()) {
                commonsLog.info("history");
                String uid = rsHistory.getString("UID");
                // 历史库位
                String historyStock = rsHistory.getString("historyStock");
                Date localtime = rsHistory.getTimestamp("localtime");
                Boolean isInsert = mes.executeUpdate("IF NOT EXISTS " +
                        "( " +
                        "   select " +
                        "       *  " +
                        "   from  " +
                        "       ItemInventoryHistories  " +
                        "   where  " +
                        "       UID = '" + uid + "' and StockLocation = '" + historyStock + "' and TimePosted_BaseDateTimeUTC = '" + localtime + "'" +
                        ") " +
                        "   insert into [dbo].[ItemInventoryHistories]" +
                        "       ([ID],[ActionEnum],[TimePosted_BaseDateTimeUTC],[Quantity],[UID],[Operator],[StockLocation])" +
                        "   values" +
                        "       (NEWID(),'8','" + localtime + "','100','" + uid + "','ding','" + historyStock + "')");
                if (isInsert) {
                    commonsLog.info("成功(history)");
                } else {
                    commonsLog.info("失败(history)");
                }
                commonsLog.info("UID: " + uid + "    historyStock: " + historyStock + "    localtime: " + localtime);
            }
            commonsLog.info("结束-----");
            // 插入数据结束时间
            long endTime = System.currentTimeMillis();
            commonsLog.info("执行耗时：" + ((endTime - startTime) / 1000) + "s");
        } catch (Exception e) {
            commonsLog.info(e);
        }
    }

    public static void main(String[] args) {
        new FetchMesDataTest().start();
    }
}
