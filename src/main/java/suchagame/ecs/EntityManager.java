package suchagame.ecs;


import suchagame.Main;
import suchagame.ecs.component.*;
import suchagame.ecs.entity.*;
import suchagame.ecs.system.StatsSystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import suchagame.utils.Utils;


import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;


public class EntityManager {

    private Map<Class<? extends Entity>, Map<String, JsonNode>> entitiesModel = new HashMap<>();

    public List<Entity> entities = new ArrayList<>();
    private int itemsCount;

    public EntityManager() {
    }

    public void initEntities() {
        addEntity(Item.class, "*");
        addEntity(MapEntity.class);
        addEntity(Player.class);
        addEntity(NPC.class);
    }


    public void addEntity(Class<? extends Entity> entityClass, String tag) {
        if (!entitiesModel.containsKey(entityClass)) {
            ObjectMapper mapper = new ObjectMapper();
            String rawEntityName = entityClass.getSimpleName().replace("Entity", "").toLowerCase();
            URL resourceUrl = Main.class.getResource(String.format("config/%s.json", rawEntityName));
            try {
                JsonNode root = mapper.readTree(Objects.requireNonNull(resourceUrl));
                Iterator<Map.Entry<String, JsonNode>> entitiesIterator = root.fields();
                entitiesModel.put(entityClass, new HashMap<>());
                while (entitiesIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entity = entitiesIterator.next();
                    entitiesModel.get(entityClass).put(entity.getKey(), entity.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (tag == null) {
            StringBuilder sb = new StringBuilder(entityClass.getSimpleName());
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
            tag = sb.toString();
        }
        if (tag.equals("*")) {
            for (Map.Entry<String, JsonNode> entity : entitiesModel.get(entityClass).entrySet()) {
                addEntity(entityClass, entity.getKey());
            }
            return;
        }
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = entitiesModel.get(entityClass).get(tag).fields();
        Map.Entry<String, JsonNode> field = fieldsIterator.next();
        Entity entity = initEntity(entityClass, field);
        this.entities.add(entity);
        Component.addNeededComponents(entity, fieldsIterator, field);

        if (entity.hasComponent(StatsComponent.class)) {
            StatsSystem.addObserver(entity);
        }
    }

    public void addEntity(Class<? extends Entity> entityClass) {
        addEntity(entityClass, null);
    }
    @SuppressWarnings("unchecked")
    public Entity initEntity(
            Class<? extends Entity> entityClass,
            Map.Entry<String, JsonNode> field)
    {
        try {
            Constructor<? extends Entity> constructor = (Constructor<? extends Entity>) entityClass.getConstructors()[0];
            if (!field.getKey().equals("metadata")) {
                return constructor.newInstance();
            }

            Class<?>[] parametersType = constructor.getParameterTypes();
            Iterator<Map.Entry<String, JsonNode>> metadataIterator = field.getValue().fields();
            Object[] args = new Object[parametersType.length];
            args = Utils.getConstructorArguments(
                    metadataIterator, constructor,
                    parametersType, args, 0
            );
            return constructor.newInstance(args);


        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public <T extends Component> List<Entity> getAllWithComponent(Class<T> componentClass) {
        List<Entity> entitiesWithComponent = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.hasComponent(componentClass))
                entitiesWithComponent.add(entity);
        }
        return entitiesWithComponent;
    }

    public Entity getLastEntity() {
        return this.entities.get(this.entities.size() - 1);
    }

    public int getEntityCount() {
        return this.entities.size();
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
