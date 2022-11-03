package ht.mapper;

import ht.entity.UrgentMaterialCheckNotOCR;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class UrgentMaterialCheckNotOCRMapper implements RowMapper{
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
        UrgentMaterialCheckNotOCR item = new UrgentMaterialCheckNotOCR();
        item.setId(rs.getInt("id"));
        item.setGRN(rs.getString("GRN"));
        item.setItemNumber(rs.getString("ItemNumber"));
        item.setReceivingLocation(rs.getString("ReceivingLocation"));
        item.setRDFinishTime(rs.getString("RDFinishTime"));
        item.setProductionTime(rs.getString("ProductionTime"));
        item.setIQCGetTime(rs.getString("IQCGetTime"));
        item.setIQCReturnTime(rs.getString("IQCReturnTime"));
        //item.setIQCCheckTime(rs.getString("IQCCheckTime"));
        //item.setIQCUploadTimeToSAP(rs.getString("IQCUploadTimeToSAP"));
        item.setIQCCheckWaitTime(rs.getString("IQCCheckWaitTime"));
        item.setPlant(rs.getString("plant"));
        item.setUID(rs.getString("UID"));
        item.setType(rs.getString("Type"));
        item.setSequence(rs.getInt("Sequence"));
        item.setGRNDATE(rs.getString("GRNDATE"));
        item.setCloseDate(rs.getString("closeDate"));
        item.setSap321122(rs.getString("sap321122"));
        item.setCreatedate(rs.getString("createdate"));
        return item;
    }

}
