package ht.mapper;

import ht.entity.ToReceiveWarehouse;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ToReceiveWarehouseMapper implements RowMapper{
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
        ToReceiveWarehouse item = new ToReceiveWarehouse();
        item.setId(rs.getInt("id"));
        item.setGRN(rs.getString("GRN"));
        item.setItemNumber(rs.getString("ItemNumber"));
        item.setGRNQuantity(rs.getString("GRNQuantity"));
        item.setReceivingLocation(rs.getString("ReceivingLocation"));
        item.setProductionTime(rs.getString("ProductionTime"));
        item.setAegisQualify(rs.getString("AegisQualify"));
        item.setSAPQualify(rs.getString("SAPQualify"));
        item.setWaitTimeToMainbin(rs.getString("WaitTimeToMainbin"));
        item.setReturnWarehouseTime(rs.getString("ReturnWarehouseTime"));
        item.setPlant(rs.getString("plant"));
        item.setUID(rs.getString("UID"));
        item.setType(rs.getString("Type"));
        item.setSequence(rs.getInt("Sequence"));
        item.setCloseDate(rs.getString("closeDate"));
        item.setCreatedate(rs.getString("createdate"));
        
        return item;
    }
}
