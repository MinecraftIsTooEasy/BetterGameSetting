package moddedmite.xylose.bettergamesetting.client.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.ChatMessageComponent;

import java.lang.reflect.Type;

public class ServerStatusResponse {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ServerStatusResponse.class, new Serializer())
            .registerTypeAdapter(Players.class, new Players.Serializer())
            .registerTypeAdapter(Version.class, new Version.Serializer())
            .create();

    private ChatMessageComponent description;
    private Players players;
    private Version version;
    private String favicon;

    public ChatMessageComponent getServerDescription() {
        return this.description;
    }

    public void setServerDescription(ChatMessageComponent description) {
        this.description = description;
    }

    public Players getPlayers() {
        return this.players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public Version getVersion() {
        return this.version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public static ServerStatusResponse fromJson(String json) {
        return GSON.fromJson(json, ServerStatusResponse.class);
    }

    public String toJson() {
        JsonObject jsonobject = new JsonObject();

        if (this.description != null) {
            jsonobject.addProperty("description", this.description.toStringWithFormatting(true));
        }

        if (this.players != null) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("max", this.players.maxPlayers());
            jsonobject1.addProperty("online", this.players.onlinePlayerCount());
            jsonobject.add("players", jsonobject1);
        }

        if (this.version != null) {
            JsonObject jsonobject2 = new JsonObject();
            jsonobject2.addProperty("name", this.version.name());
            jsonobject2.addProperty("protocol", this.version.protocol());
            jsonobject.add("version", jsonobject2);
        }

        if (this.favicon != null) {
            jsonobject.addProperty("favicon", this.favicon);
        }

        return jsonobject.toString();
    }
    
    public record Players(int maxPlayers, int onlinePlayerCount) {
        
        public static class Serializer implements JsonDeserializer<Players> {
                @Override
                public Players deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject jsonobject = element.getAsJsonObject();
                    return new Players(jsonobject.get("max").getAsInt(), jsonobject.get("online").getAsInt());
                }
            }
        }
    
    public record Version(String name, int protocol) {
        
        public static class Serializer implements JsonDeserializer<Version> {
                @Override
                public Version deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject jsonobject = element.getAsJsonObject();
                    return new Version(jsonobject.get("name").getAsString(), jsonobject.get("protocol").getAsInt());
                }
            }
        }

    public static class Serializer implements JsonDeserializer<ServerStatusResponse> {
        @Override
        public ServerStatusResponse deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = element.getAsJsonObject();
            ServerStatusResponse serverstatusresponse = new ServerStatusResponse();

            if (jsonobject.has("description")) {
                JsonElement description = jsonobject.get("description");
                serverstatusresponse.setServerDescription(description.isJsonPrimitive() ? ChatMessageComponent.createFromText(description.getAsString()) : ChatMessageComponent.createFromJson(description.toString()));
            }

            if (jsonobject.has("players")) {
                serverstatusresponse.setPlayers(context.deserialize(jsonobject.get("players"), Players.class));
            }

            if (jsonobject.has("version")) {
                serverstatusresponse.setVersion(context.deserialize(jsonobject.get("version"), Version.class));
            }

            if (jsonobject.has("favicon")) {
                serverstatusresponse.setFavicon(jsonobject.get("favicon").getAsString());
            }

            return serverstatusresponse;
        }
    }
}
