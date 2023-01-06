package depo_market.depo_market_1_16_5;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * アイテムの値段を保持するオブジェクトを保持し、値段に関する処理を行うオブジェクト。
 */
public class MarketOperator {

    private boolean market_run_flag = false;//取引停止と取引可能のフラグ
    private Map<String, ItemPrice> ItemDataList;//アイテムの値段を保持するオブジェクトのマップ
    private final DataBaseTradeItem dataBaseTradeItem;//アイテムの値段の初期値を得るため
    public MarketOperator(ArrayList<ItemMenuSlot> initialPriceList){
        this.ItemDataList = new HashMap<>();
        this.dataBaseTradeItem = new DataBaseTradeItem();
        for(ItemMenuSlot Item : initialPriceList){
            this.ItemDataList.put(Item.getEnName(),new ItemPrice(Item.getInitialPrice()));
        }
    }
    public void loadData(boolean isRun, Map<String, ItemPrice> data){
        market_run_flag = isRun;
        for(String key : data.keySet()) {
            ItemDataList.put(key, data.get(key));
        }
    }
    public void Initialize(ArrayList<ItemMenuSlot> initialPriceList) {
        ItemDataList = new HashMap<>();
        for (ItemMenuSlot Item : initialPriceList) {
            this.ItemDataList.put(Item.getEnName(), new ItemPrice(Item.getInitialPrice()));
        }
    }
    public void StartMarket(Player player) {
        if (!market_run_flag) {
            player.sendMessage("市場開始");
            market_run_flag = true;
        } else {
            player.sendMessage("市場はすでに起動中です");
        }
    }
    public void StopMarket(Player player){
        if(market_run_flag) {
            market_run_flag = false;
            player.chat("市場停止");
        }else {
            player.sendMessage("市場はすでに停止です");
        }
    }
    public float getPrice(String nameEn){
        return ItemDataList.getOrDefault(nameEn,new ItemPrice(0f)).getPrice();
    }
    public boolean getMarketState(){
        return market_run_flag;
    }
    public Map<String, ItemPrice> getData(){
        return ItemDataList;
    }

    //アイテムの買うとき取引量を記録。値段変動メソッドを呼び出す
    public float buy(String nameEn,int Amount){
        float price = getWholePrice(nameEn,Amount);
        ItemDataList.get(nameEn).addAmountOfBought(Amount);
        return price;
    }

    //アイテムの売るとき取引量を記録。値段変動メソッドを呼び出す
    public float sell(String nameEn,int Amount){
        float price = getWholePrice(nameEn, Amount);
        ItemDataList.get(nameEn).addAmountOfSold(Amount);
        return price;
    }

    //値段の変動を行なわない。
    private float getWholePrice(String nameEn, int Amount){
        ItemPrice itemPrice = ItemDataList.get(nameEn);
        float price = itemPrice.getPrice();
        return price * Amount;
    }
}
