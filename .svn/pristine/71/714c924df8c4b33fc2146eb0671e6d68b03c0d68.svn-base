package ht.mapper;

import ht.entity.UrgentMaterialCheckOCR;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;



public class UrgentMaterialCheckOCRMapper implements RowMapper{
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
        UrgentMaterialCheckOCR item = new UrgentMaterialCheckOCR();
        item.setId(rs.getInt("id"));
        item.setGRN(rs.getString("GRN"));
        item.setItemNumber(rs.getString("ItemNumber"));
        item.setReceivingLocation(rs.getString("ReceivingLocation"));
        item.setGRNQuantity(rs.getString("GRNQuantity"));
        item.setUIDQuantity(rs.getString("UIDQuantity"));
        item.setRDFinishTime(rs.getString("RDFinishTime"));
        item.setProductionTime(rs.getString("ProductionTime"));
        item.setOCRCheckWaitTime(rs.getString("OCRCheckWaitTime"));
        item.setAuditDataTime(rs.getString("auditDataTime"));
        item.setPlant(rs.getString("plant"));
        item.setUID(rs.getString("UID"));
        item.setType(rs.getString("Type"));
        item.setSequence(rs.getInt("Sequence"));
        
        item.setCloseDate(rs.getString("closeDate"));
        item.setSap321122(rs.getString("sap321122"));
        item.setCreatedate(rs.getString("createdate"));
        
        return item;
    }

}
