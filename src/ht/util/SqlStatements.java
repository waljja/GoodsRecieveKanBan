package ht.util;

public class SqlStatements {
    // 查找工厂-仓位，ToStock_Input需要剪切字符串
    public static String findStock(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select " +
                "ToStock_Input as stockname " +
                "from " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] " +
                "where " +
                "UID = '" + uid + "' " +
                "and" +
                "(ToStock_Input is NOT NULL " +
                "or " +
                "ToStock_Input != '')");
        return sbsql.toString();
    }

    // 查找空的工厂-仓位，ToStock_Input需要剪切字符串
    public static String findStockNull(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select " +
                "ToStock_Input as stockname " +
                "from " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] " +
                "where " +
                "UID = '" + uid + "' " +
                "and " +
                "(ToStock_Input is NULL " +
                "or " +
                "ToStock_Input = '')");
        return sbsql.toString();
    }

    // 查找uid和对应仓位，ToStock_Input需要剪切字符串
    public static String findUidStock(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select top 1 " +
                "UID, ToStock_Input " +
                "from " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM " +
                "where " +
                "ToStock_Input like'%QM%' " +
                "and " +
                "UID = '" + uid + "'");
        return sbsql.toString();
    }

    // 查找最早的requireTime
    public static String findEarliestReqTime(String partNumber,String nowDay){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select top 1 " +
                "RequireTime " +
                "from " +
                "[HonortoneMesDb].[dbo].[xTend_MissingMaterials]  " +
                "where " +
                "PartNumber = '" + partNumber + "' " +
                "and " +
                "convert(varchar(10),RequireTime,23) ='" + nowDay + "' " +
                "order by " +
                "RequireTime ");
        return sbsql.toString();
    }

    // 查找时间最晚的移库信息
    public static String findLatestTransInfo(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select top 1 " +
                "UXM.UID, ToStock_Input, IIH.StockResource as 'historyStock', DATEADD(HOUR, 8, TransactionTime_1) AS 'localtime' " +
                "from " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM " +
                "left join " +
                "[HonortoneMesDb].[dbo].[ItemInventoryHistories] IIH " +
                "on " +
                "UXM.UID = IIH.UID " +
                "collate " +
                "chinese_prc_ci_as " +
                "where " +
                "(IIH.StockResource is not null and IIH.StockResource like '%IQ%') " +
                "and " +
                "UXM.UID in ('"+ uid +"')  " +
                "order by " +
                "IIH.TimePosted_BaseDateTimeUTC " +
                "desc");
        return sbsql.toString();
    }

    // 查找时间最晚的移库信息
    public static String findLatestTransInfo1(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select top 1 " +
                "UXM.UID, ToStock_Input, IIH.StockResource as 'historyStock', DATEADD(HOUR, 8, TransactionTime_1) AS 'localtime' " +
                "from " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM " +
                "left join " +
                "[HonortoneMesDb].[dbo].[ItemInventoryHistories] IIH " +
                "on " +
                "UXM.UID = IIH.UID " +
                "collate " +
                "chinese_prc_ci_as " +
                "where " +
                "IIH.StockResource is not null " +
                "and " +
                "UXM.UID in ('"+ uid +"') " +
                "order by " +
                "IIH.TimePosted_BaseDateTimeUTC " +
                "desc");
        return sbsql.toString();
    }

    // 查找时间最晚的移库信息
    public static String findLatestTransInfo2(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select top 1 " +
                "UXM.UID, ToStock_Input, IIH.StockResource as 'historyStock', DATEADD(HOUR, 8, TransactionTime_1) AS 'localtime' " +
                "from " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM " +
                "left join " +
                "[HonortoneMesDb].[dbo].[ItemInventoryHistories] IIH " +
                "on " +
                "UXM.UID = IIH.UID " +
                "collate " +
                "chinese_prc_ci_as " +
                "where " +
                "IIH.StockResource like '%IQ%' " +
                "and " +
                "UXM.UID in ('"+ uid +"')  " +
                "order by " +
                "IIH.TimePosted_BaseDateTimeUTC " +
                "desc");
        return sbsql.toString();
    }

    /**
     * @description 查找移库信息,按时间排序
     * @param uid
     * @return SQL
     */
    public static String findTransOrderByTransTime(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select UID, ToStock_Input as StockLocation, TransactionTime AS 'localtime'  " +
                "from  " +
                "[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM   " +
                "where  " +
                "UID = '"+ uid +"'  " +
                "and  " +
                "ToStock_Input is not null  " +
                "and  " +
                "(ToStock_Input like'%RH%' or ToStock_Input like'%CU%')  " +
                "order by  " +
                "TransactionTime desc");
        return sbsql.toString();
    }

    /**
     * @description FRB.name和StockLocation都在ToStock_Input中,StockLocation不需要剪切
     * @param uid
     * @return SQL
     */
    public static String findStockInfo(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select top 1  " +
                "ToStock_Input, DATEADD(HOUR, 8, TimePosted_BaseDateTimeUTC) AS 'localtime'  " +
                "from  " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM  " +
                "left join  " +
                "[HonortoneMesDb].[dbo].[ItemInventoryHistories] IIH  " +
                "on  " +
                "UXM.UID = IIH.UID  " +
                "collate chinese_prc_ci_as  " +
                "where  " +
                "ToStock_Input like '%QM%'  " +
                "and  " +
                "UXM.UID in ('"+ uid +"')  " +
                "order by " +
                "TimePosted_BaseDateTimeUTC  " +
                "desc");
        return sbsql.toString();
    }

    // 查找IQC归还时间，根据越南版本修改
    public static String findIqcReturnTime(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select  " +
                "UID, ToStock_Input as StockLocation, TransactionTime AS 'localtime'  " +
                "from  " +
                "[HonortoneMesDb].[dbo].[UID_xTend_MaterialTransactionsRHDCDW]  " +
                "where  " +
                "UID = '"+ uid +"'  " +
                "and  " +
                "ToStock_Input is not null  " +
                "and  " +
                "ToStock_Input like'%QM%'  " +
                "order by  " +
                "TransactionTime desc");
        return sbsql.toString();
    }

    // 根据收货编号查找收货时间（vendorrid）
    public static String findGRDate_v(String grn){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select CreateDate from [imslabel].[dbo].[vendorrid] where GRN = '"+ grn +"'");
        return sbsql.toString();
    }

    // 根据收货编号查找收货时间（pcbvendorrid）
    public static String findGRDate_PV(String grn){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select CreateDate from [imslabel].[dbo].[pcbvendorrid] where GRN = '"+ grn +"'");
        return sbsql.toString();
    }
}
