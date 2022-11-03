package ht.mapper;

import ht.entity.NotFinishSO;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class NotFinishSOMapper implements RowMapper{
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
        NotFinishSO nfs = new NotFinishSO();
        nfs.setId(rs.getInt("id"));
        nfs.setSo(rs.getString("so"));
        nfs.setPlant(rs.getString("plant"));
        nfs.setPn(rs.getString("pn"));
        nfs.setBom(rs.getString("bom"));
        nfs.setNeedQty(rs.getString("needQty"));
        nfs.setGotQty(rs.getString("gotQty"));
        nfs.setInventory(rs.getString("inventory"));
        nfs.setSoStartDate(rs.getString("soStartDate"));
        return nfs;
    }
}
