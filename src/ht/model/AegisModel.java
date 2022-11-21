package ht.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Aegis model
 *
 * @author 丁国钊
 * @date 2022-11-21
 */
@Setter
@Getter
public class AegisModel {
    /** FactoryResourceBases表，库位，对应 ToStock_Input */
    private String name;
    /** ItemInventories表的identifier字段，对应 UID */
    private String identifier;
    /** StockLocation表的identifier字段，对应 historyStock */
    private String historyStock;
    /** ItemInventoryHistories表，对应 TransactionTime */
    private String localtime;
}
