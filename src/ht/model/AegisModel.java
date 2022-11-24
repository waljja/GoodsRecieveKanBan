package ht.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Aegis model
 *
 * @author 丁国钊
 * @date 2022-11-21
 */
@Setter
@Getter
public class AegisModel {
    private String uid;
    /** 库位 */
    private String toStockInput;
    /** StockLocation表的identifier字段，对应 historyStock */
    private String historyStock;
    /** vps 表的 upAegisDate，上传 Aegis 时间 */
    private Date transactionTime;
    /** ItemInventoryHistories表，对应 TransactionTime */
    private Date localtime;
}
