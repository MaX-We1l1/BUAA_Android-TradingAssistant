package com.example.myapplication.home;

import static com.example.myapplication.MainActivity.getCurrentUserId;
import static com.example.myapplication.MainActivity.getCurrentUsername;
import static com.example.myapplication.R.*;
import static com.example.myapplication.database.DBFunction.getCommodity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.myapplication.commodity.AddCommodityActivity;
import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.database.CartItem;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.Hobby;
import com.example.myapplication.database.OrderTable;
import com.example.myapplication.database.Type;
import com.example.myapplication.profile.ProfileActivity;
import com.example.myapplication.square.CommodityAdapter;
import com.example.myapplication.square.CommodityListActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.Choice;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.zhipu.oapi.service.v4.model.ModelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import okhttp3.ConnectionPool;
import okhttp3.Response;

public class HomepageActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextView textApiResult;
    private TextView textRecommendations;
    private RecyclerView recyclerView;
    private List<Commodity> recommendList = new ArrayList<>();
    private CommodityAdapter commodityAdapter;
    ClientV4 client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        String apiKey = getString(R.string.api_key);
        client = new ClientV4.Builder(apiKey)
                .enableTokenCache()
                .networkConfig(300, 100, 100, 100, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 1, TimeUnit.SECONDS))
                .build();

        EditText editQueryInput = findViewById(R.id.edit_query_input);
        Button buttonCallApi = findViewById(R.id.button_call_api);
        textApiResult = findViewById(R.id.text_api_result);
        textRecommendations = findViewById(R.id.text_recommendation);

        SensitiveWordManager sensitiveWordManager = new SensitiveWordManager(this);
        String[] wordFiles = {"COVID-19词库.txt", "GFW补充词库.txt", "其他词库.txt",
                 "民生词库.txt", "补充词库.txt", "贪腐词库.txt", "零时-Tencent.txt"};
        for (String file : wordFiles) {
            sensitiveWordManager.loadSensitiveWords(file);
        }

        buttonCallApi.setOnClickListener(v -> {
            String query = editQueryInput.getText().toString();

            if (query.isEmpty()) {
                Toast.makeText(HomepageActivity.this, "输入不能为空" , Toast.LENGTH_SHORT).show();
                return;
            }

            if (sensitiveWordManager.containsSensitiveWord(query)) {
                Toast.makeText(HomepageActivity.this, "输入内容包含敏感信息，请修改后再试",Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                callZhipuApi(query);
            } catch (JSONException e) {
                e.printStackTrace();
                textApiResult.setText("Error JSON: " + e.getMessage());
            }
        });

        if (LitePal.findAll(Commodity.class).isEmpty()) {
//            Log.e("Fuck" , "Empty Commodity List");
            textRecommendations.setText("商品列表中暂时为空");
        }
        else {
            textRecommendations.setText("推荐商品列表：\n");
            String userName = getCurrentUsername();
            Long userId = getCurrentUserId();

            // 收藏列表
            List<Hobby> hobbies = DBFunction.findHobbyByName(userName);
            // 购物车项列表
            List<CartItem> cartItems = DBFunction.getCart(userName);
            // 订单列表
            List<OrderTable> orderTables = DBFunction.getOrdersFromUser(userId);
            List<Commodity> hobbyCommodities = new ArrayList<>();
            List<Commodity> cartItemCommodities = new ArrayList<>();
            List<Commodity> orderTableCommodities = new ArrayList<>();
            if (hobbies != null) {
                for (Hobby hobby : hobbies) {
                    hobbyCommodities.add(getCommodity(hobby.getCommodityId()));
                }
            }
            if (cartItems != null) {
                for (CartItem cartItem : cartItems) {
                    cartItemCommodities.add(getCommodity(cartItem.getCommodityId()));
                }
            }
            for (OrderTable orderTable : orderTables) {
                orderTableCommodities.add(getCommodity(orderTable.getCommodityId()));
            }
            List<Commodity> allCommodities = LitePal.findAll(Commodity.class);

            JSONObject userFeatureJSON = null;

            recommendList.clear();
            try {
                userFeatureJSON = buildUserFeatureJson(hobbyCommodities, cartItemCommodities, orderTableCommodities, allCommodities);
            } catch (JSONException e) {
                textRecommendations.setText("userFeatureJSON Error: " + e.getMessage());
                e.printStackTrace();
            }
            Log.e("FUCK", userFeatureJSON.toString());
            String prompt = null;
            try {
                prompt = buildModelInput(userFeatureJSON);
            } catch (JSONException e) {
                textRecommendations.setText("prompt Error: " + e.getMessage());
                Log.e("SUCK", e.getMessage());
            }
            Log.e("prompt : ", prompt);
            // 调用模型接口，获取推荐列表
            try {
                commodityRecommendationSystem(prompt);
            } catch (JSONException e) {
                textRecommendations.setText("commodityRecommendationSystem Error: "  + e.getMessage());
                e.printStackTrace();
            }
        }
        // 这里可以设置其他初始化逻辑，比如加载数据等
        Button messagesButton = findViewById(R.id.button_messages);
        messagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        Button userButton = findViewById(R.id.button_profile);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        Button sellButton = findViewById(R.id.button_sell);
        sellButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, AddCommodityActivity.class);
            startActivity(intent);
        });

        Button squareButton = findViewById(R.id.button_square);
        squareButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, CommodityListActivity.class);
            startActivity(intent);
        });

        Button homeButton = findViewById(R.id.button_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, HomepageActivity.class);
            startActivity(intent);
        });
    }

    private JSONObject buildUserFeatureJson(
            List<Commodity> hobbyCommodities,
            List<Commodity> cartItemCommodities,
            List<Commodity> orderTableCommodities,
            List<Commodity> allCommodities
    ) throws JSONException {
        JSONObject userFeature = new JSONObject();
        JSONArray favoriteItems = buildCommodityJsonArray(hobbyCommodities);
        JSONArray cartItems = buildCommodityJsonArray(cartItemCommodities);
        JSONArray orderItems = buildCommodityJsonArray(orderTableCommodities);
        JSONArray allItems = buildCommodityJsonArray(allCommodities);
        userFeature.put("收藏商品", favoriteItems);
        userFeature.put("购物车中商品" , cartItems);
        userFeature.put("订单中的商品", orderItems);
        userFeature.put("喜欢的类别", getPreferredCategories
                (orderTableCommodities,cartItemCommodities,hobbyCommodities));
        userFeature.put("系统中所有商品", allItems);
        return userFeature;
    }

    private JSONArray buildCommodityJsonArray(List<Commodity> commodities) throws JSONException {
        JSONArray commodityArray = new JSONArray();
        for (Commodity commodity : commodities) {
            JSONObject commodityJson = new JSONObject();
            commodityJson.put("名字", commodity.getCommodityName());
            commodityJson.put("价格", commodity.getPrice());
            commodityJson.put("类别", commodity.getType().name());
            commodityJson.put("描述", commodity.getDescription());
            commodityJson.put("ID", commodity.getId());
            commodityArray.put(commodityJson);
        }
        return commodityArray;
    }

    private JSONArray getPreferredCategories(List<Commodity> commodities1,
                                             List<Commodity> commodities2, List<Commodity> commodities3) {
        // 用于统计各类别的购买次数
        Map<String, Integer> categoryCountMap = new HashMap<>();

        for (Commodity commodity : commodities1) {
            String category = commodity.getType().name();
            categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
        }

        for (Commodity commodity : commodities2) {
            String category = commodity.getType().name();
            categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
        }

        for (Commodity commodity : commodities3) {
            String category = commodity.getType().name();
            categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
        }

        // 排序，优先选择购买频率最高的类别
        List<Map.Entry<String, Integer>> sortedCategories = new ArrayList<>(categoryCountMap.entrySet());
        sortedCategories.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        // 选择前 N 个类别作为偏好（这里假设最多取 3 个）
        JSONArray preferredCategories = new JSONArray();
        int maxCategories = 3;
        for (int i = 0; i < Math.min(maxCategories, sortedCategories.size()); i++) {
            preferredCategories.put(sortedCategories.get(i).getKey());
        }

        return preferredCategories;
    }

    private String buildModelInput(JSONObject userFeature) throws JSONException {
        StringBuilder prompt = new StringBuilder();
        prompt.append("您是智能购物助手。根据用户的行为数据和详细的产品信息，推荐他们可能喜欢的 5 款产品，如果系统中所有商品的数量不够则推荐系统中所有商品。\n");
        prompt.append("下面是产品相关数据：\n");

        prompt.append("### 用户的收藏:\n");
        prompt.append(formatrecommendList(userFeature.getJSONArray("收藏商品"))).append("\n");

        prompt.append("### 用户的购物车中商品:\n");
        prompt.append(formatrecommendList(userFeature.getJSONArray("购物车中商品"))).append("\n");

        prompt.append("### 用户订单中的商品:\n");
        prompt.append(formatrecommendList(userFeature.getJSONArray("订单中的商品"))).append("\n");

        prompt.append("### 用户喜欢的类别:\n");
        prompt.append(userFeature.getJSONArray("喜欢的类别")).append("\n");

        prompt.append("### 系统中所有商品:\n");
        prompt.append(formatrecommendList(userFeature.getJSONArray("系统中所有商品"))).append("\n");

        prompt.append("不要重复输出商品id，如果商品数量不够不用输出五个，输出推荐结果时，遵循以下JSON格式，例如：\n");
        prompt.append("[\n");
        prompt.append("    {\"商品ID\": \"123\"},\n");
        prompt.append("    {\"商品ID\": \"456\"}\n");
        prompt.append("]\n");
        prompt.append("请确保返回 JSON 格式化的推荐结果。商品id必须在系统中所有商品中出现。严格按照以上JSON格式，不要有任何注释，数量不够可以不用五个\n");

        return prompt.toString();
    }

    private String formatrecommendList(JSONArray commodities) throws JSONException {
        StringBuilder formattedList = new StringBuilder();
        for (int i = 0; i < commodities.length(); i++) {
            JSONObject commodity = commodities.getJSONObject(i);
            formattedList.append("- 名字: ").append(commodity.getString("名字")).append("\n");
            formattedList.append("  价格: ").append(commodity.getDouble("价格")).append("\n");
            formattedList.append("  类别: ").append(commodity.getString("类别")).append("\n");
            formattedList.append("  描述: ").append(commodity.getString("描述")).append("\n");
            formattedList.append("  ID: ").append(commodity.getInt("ID")).append("\n\n");
        }
        return formattedList.toString();
    }

    private void updateRecylerView() {

        Log.e("recommendListAfter : ", recommendList.toString());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commodityAdapter = new CommodityAdapter(this, recommendList, REQUEST_CODE); // 创建适配器
        recyclerView.setAdapter(commodityAdapter); // 设置适配器
    }

    private void commodityRecommendationSystem(String prompt) throws JSONException {
        executorService.execute(() -> {
            try {
                ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
                List<ChatMessage> messageList = new ArrayList<>();
                messageList.add(chatMessage);
                String requestId = String.format("BUAA_Android-TradingAssistant : " + System.currentTimeMillis());

                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model("glm-4-flash")
                        .stream(Boolean.FALSE)
                        .invokeMethod(Constants.invokeMethod)
                        .messages(messageList)
                        .requestId(requestId)
                        .build();

                ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
                ModelData modelData = invokeModelApiResp.getData();

                Choice firstChoice = modelData.getChoices().get(0);
                String content = (String) firstChoice.getMessage().getContent();
                int startIndex = content.indexOf('[');
                int endIndex = content.lastIndexOf(']');
                content = content.substring(startIndex, endIndex + 1);
                String finalContent = content;
//                mainHandler.post(() -> textRecommendations.setText("model output: " + finalContent));
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonArray = mapper.readTree(finalContent);

//                Log.e("jsonArray : ", jsonArray.toString());
                if (jsonArray.isArray()) {
                    for (JsonNode jsonNode : jsonArray) {
                        Commodity commodity = DBFunction.getCommodity(Long.parseLong(jsonNode.get("商品ID").asText()));
                        recommendList.add(commodity);
                        //Log.e("NOTICE : ", commodity.toString());
                    }
                }

                mainHandler.post(() -> {
                    updateRecylerView();
                });
                Log.e("recommendListFUCK : ", recommendList.toString());
//                mainHandler.post(() -> textRecommendations.setText("CommodityRecommendationSystem : " + content));
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> textRecommendations.setText("Error: " + e.getMessage()));
            }
        });
    }

    private void callZhipuApi(String query) throws JSONException {
        executorService.execute(() -> {
            try {
                ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), query);
                List<ChatMessage> messageList = new ArrayList<>();
                messageList.add(chatMessage);
                String requestId = String.format("BUAA_Android-TradingAssistant : " + System.currentTimeMillis());

                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model("glm-4-flash")
                        .stream(Boolean.FALSE)
                        .invokeMethod(Constants.invokeMethod)
                        .messages(messageList)
                        .requestId(requestId)
                        .build();

                ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
                ModelData modelData = invokeModelApiResp.getData();

                Choice firstChoice = modelData.getChoices().get(0);
                String content = (String) firstChoice.getMessage().getContent();
                mainHandler.post(() -> textApiResult.setText("model output: " + content));
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> textApiResult.setText("Error: " + e.getMessage()));
            }
        });
    }
}