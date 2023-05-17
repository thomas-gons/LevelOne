package suchagame.ecs;


import com.fasterxml.jackson.databind.node.ObjectNode;
import suchagame.Main;
import suchagame.ecs.component.*;
import suchagame.ecs.entity.*;
import suchagame.ecs.system.StatsSystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;
import java.net.URL;
import java.util.*;


public class EntityManager {

    private Map<Class<? extends Entity>, JsonNode> entitiesModel = new HashMap<>();

    public List<Entity> entities = new ArrayList<>();
    private int itemsCount;

    public EntityManager() {
    }

    public void initEntities() {
        int mobCount = 256;
        initItems();
        this.addEntity(new Player());
        this.addEntity(new MapEntity());
        this.addEntity(new NPC());
        for (int i = 0; i < mobCount; i++) {
            this.addEntity(new Mob());
        }
    }
    @SuppressWarnings("unchecked")
    private void initItems() {
        ObjectMapper mapper = new ObjectMapper();
        URL resourceUrl = Main.class.getResource("config/items.json");
        JsonNode root;
        try {
            root = mapper.readTree(resourceUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Iterator<JsonNode> itemsIterator = root.elements();
        while (itemsIterator.hasNext()) {
            JsonNode entry = itemsIterator.next();
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = entry.fields();

            String tag = fieldsIterator.next().getValue().asText();
            String type = fieldsIterator.next().getValue().asText();
            int slimeDropValue = fieldsIterator.next().getValue().asInt();
            Item item = new Item(tag, type, slimeDropValue);
            if (fieldsIterator.hasNext())
                item.addComponent(new StatsComponent(
                        (HashMap<String, Float>) mapper.convertValue(fieldsIterator.next().getValue(), HashMap.class),
                        null
                ));
            entities.add(item);
        }
        itemsCount = Entity.globalID;
    }

    public <T extends Component> List<Entity> getAllWithComponent(Class<T> componentClass) {
        List<Entity> entitiesWithComponent = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.hasComponent(componentClass))
                entitiesWithComponent.add(entity);
        }
        return entitiesWithComponent;
    }

    public Item[] getAllItems() {
        Item[] items = new Item[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            items[i] = (Item) this.entities.get(i);
        }
        return items;
    }

    public MapEntity getMap() {
        return (MapEntity) this.entities.get(itemsCount);
    }
    public Player getPlayer() {
        return (Player) this.entities.get(itemsCount + 1);
    }
    public NPC getNPC() {
        return (NPC) this.entities.get(itemsCount + 2);
    }
    @SuppressWarnings("unchecked")
    public void addEntity(Entity entity) {
        if (!entitiesModel.containsKey(entity.getClass())) {
            ObjectMapper mapper = new ObjectMapper();
            URL resourceUrl = Main.class.getResource(String.format("config/%s.json", entity.getClass().getSimpleName().toLowerCase()));
            try {
                JsonNode root = mapper.readTree(Objects.requireNonNull(resourceUrl));
                entitiesModel.put(entity.getClass(), root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = entitiesModel.get(entity.getClass()).fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            String rawClassName = String.format("suchagame.ecs.component.%sComponent", field.getKey());
            try {
                Class<? extends Component> componentClass = (Class<? extends Component>) Class.forName(rawClassName);
                Component.instantiateComponent(entity, componentClass, field.getValue());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.entities.add(entity);
        if (entity.hasComponent(StatsComponent.class)) {
            StatsSystem.addObserver(entity);
        }
    }
    @SuppressWarnings("unchecked")

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public int getEntityCount() {
        return this.entities.size();
    }

    public Item getItem(String tag) {
        for (Entity entity : this.entities) {
            if (entity instanceof Item item) {
                if (Objects.equals(item.getTag(), tag)) {
                    return item;
                }
            } else {
                break;
            }
        }
        return null;
    }
}
