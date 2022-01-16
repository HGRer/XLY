package homework.week11.pubsub.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import homework.week11.pubsub.entity.Order;

public class JsonUtil {
    static {
        ParserConfig.getGlobalInstance().addAccept("homework.week11.pubsub.entity");
    }
    
    public static String orderToJson(Order order) {
    	return JSON.toJSONString(order);
    }
    
    public static Order jsonToOrder(String str) {
    	return JSON.parseObject(str, Order.class);
    }
}
