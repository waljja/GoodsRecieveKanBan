package ht.util;

public class SqlStatements {
    // 查找工厂-仓位
    public static String findStock(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select " +
                "ToStock_Input as stockname" +
                "from " +
                "[dbo].[UID_xTend_MaterialTransactionsRHDCDW]" +
                "where" +
                "UID = '" + uid + "'" +
                "and" +
                "(ToStock_Input is NOT NULL" +
                "or " +
                "ToStock_Input != '')");

        return sbsql.toString();
    }

    // 查找空的工厂-仓位
    public static String findStockNull(String uid){
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select " +
                "ToStock_Input as stockname" +
                "from " +
                "[dbo].[UID_xTend_MaterialTransactionsRHDCDW]" +
                "where" +
                "UID = '" + uid + "'" +
                "and" +
                "(ToStock_Input is NULL" +
                "or " +
                "ToStock_Input = '')");

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
}
