package ht.util;

public class SqlStatements {
    // 查找工厂-仓位，ToStock_Input需要剪切字符串
    public static String findStock(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select " +
                "ToStock_Input as stockname " +
                "from " +
                "[dbo].[UID_xTend_MaterialTransactionsRHDCDW] " +
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
                "[dbo].[UID_xTend_MaterialTransactionsRHDCDW] " +
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
                "UID_xTend_MaterialTransactionsRHDCDW UXM " +
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
                "[dbo].[xTend_MissingMaterials]  " +
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
                "UID_xTend_MaterialTransactionsRHDCDW UXM " +
                "left join " +
                "ItemInventoryHistories IIH " +
                "on " +
                "UXM.UID = IIH.UID " +
                "collate " +
                "chinese_prc_ci_as " +
                "where " +
                "(IIH.StockResource is not null and IIH.StockResource like '%IQ%') " +
                "and " +
                "UXM.UID in ("+ uid +")  " +
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
                "UID_xTend_MaterialTransactionsRHDCDW UXM " +
                "left join " +
                "ItemInventoryHistories IIH " +
                "on " +
                "UXM.UID = IIH.UID " +
                "collate " +
                "chinese_prc_ci_as " +
                "where " +
                "IIH.StockResource is not null " +
                "and " +
                "UXM.UID in ("+ uid +") " +
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
                "UID_xTend_MaterialTransactionsRHDCDW UXM " +
                "left join " +
                "ItemInventoryHistories IIH " +
                "on " +
                "UXM.UID = IIH.UID " +
                "collate " +
                "chinese_prc_ci_as " +
                "where " +
                "IIH.StockResource like '%IQ%' " +
                "and " +
                "UXM.UID in ("+ uid +")  " +
                "order by " +
                "IIH.TimePosted_BaseDateTimeUTC " +
                "desc");

        return sbsql.toString();
    }
}
