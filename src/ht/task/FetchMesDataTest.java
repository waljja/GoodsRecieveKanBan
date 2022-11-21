package ht.task;

import ht.mappergenerator.AegisMapper;
import ht.mappergenerator.mapper26.VpsMapper;
import ht.mappergenerator.mapper33.ToReceiveCheckMapper;
import ht.model.AegisModel;
import ht.model.model26.Vps;
import ht.model.model33.ToReceiveCheck;
import ht.model.model33.ToReceiveCheckExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务，获取数据
 * <p>
 * 测试 MES 2.0 收货看板
 *
 * @author 丁国钊
 * @date 2022-11-18
 */
public class FetchMesDataTest {
    @Autowired
    VpsMapper vpsMapper;
    @Autowired
    AegisMapper aegisMapper;
    @Autowired
    ToReceiveCheckMapper toReceiveCheckMapper;

    /**
     * 获取 Aegis 库位信息
     * <p>
     * 插入UID_xTend_MaterialTransactionsRHDCDW, ItemInventoryHistories表
     * <p>
     */
    public void start() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        // 今天
        String currentDate = simpleDateFormat.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -2);
        // 前天
        String dateSub2 = simpleDateFormat.format(cal.getTime());
        List<Vps> vpsList;
        List<Vps> grnList = new ArrayList<>();
        List<AegisModel> aegisModelList;
        List<ToReceiveCheck> toReceiveCheckList;
        // 查询两天内 GRN 数据（待点收）
        vpsList = vpsMapper.selectGrnByDate(currentDate, dateSub2);
        for (Vps vps : vpsList) {
            String grn = vps.getGRN();
            // 根据 GRN 查看板表未关闭工单
            ToReceiveCheckExample toReceiveCheckExample = new ToReceiveCheckExample();
            toReceiveCheckExample.createCriteria().andGRNEqualTo(grn).andCloseDateIsNotNull();
            toReceiveCheckList = toReceiveCheckMapper.selectByExample(toReceiveCheckExample);
            // 只拿没有关闭的GRN和UID
            if (toReceiveCheckList.size() >= 0 && toReceiveCheckList != null) {
                Vps vps1 = new Vps();
                vps1.setGRN(vps.getGRN());
                vps1.setRid(vps.getRid());
                grnList.add(vps1);
            }
        }
        for (Vps vps : grnList) {
            // 根据 UID 获取 Aegis 库位信息
            aegisModelList = aegisMapper.selectStockByIdentifier(vps.getRid());
            for (int i =0 ; i < aegisModelList.size(); i++) {
                System.out.println(aegisModelList.get(i).getName());
            }
        }
        // 26 获取 vendorrid, pcbvendorrid 表数据
        // 33 获取 NotFinishSO, ToReceiveCheck, ToReceiveWarehouse, ToReceiveWarehouseB, UrgentMaterialCheckOCR, UrgentMaterialCheckNotOCR 表数据
    }

    public static void main(String[] args) {
        new FetchMesDataTest().start();
    }
}
