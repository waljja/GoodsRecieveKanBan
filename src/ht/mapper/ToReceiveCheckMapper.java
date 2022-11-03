package ht.mapper;

import ht.entity.ToReceiveCheck;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ToReceiveCheckMapper implements RowMapper{
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
        ToReceiveCheck item = new ToReceiveCheck();
        item.setId(rs.getInt("id"));
        item.setGRN(rs.getString("GRN"));
        item.setItemNumber(rs.getString("ItemNumber"));
        item.setGRNQuantity(rs.getString("GRNQuantity"));
        item.setUIDQuantity(rs.getString("UIDQuantity"));
        item.setProductionTime(rs.getString("ProductionTime"));
        item.setWaittime(rs.getString("WaitTime"));
        item.setPlant(rs.getString("plant"));
        item.setUID(rs.getString("UID"));
        item.setGRNDATE(rs.getString("GRNDATE"));
        item.setGRN103(rs.getString("GRN103"));
        item.setType(rs.getString("Type"));
        item.setSequence(rs.getInt("sequence"));
        item.setCreatedate(rs.getString("createdate"));
        item.setCloseDate(rs.getTimestamp("closeDate"));
        return item;
    }
}
