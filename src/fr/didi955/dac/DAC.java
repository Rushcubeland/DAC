package fr.didi955.dac;

import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.PlayerTurn;
import fr.didi955.dac.listeners.*;
import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.tools.scoreboard.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DAC extends JavaPlugin {

    private static DAC instance;
    private GameState gameState;
    private PlayerTurn playerTurn;
    private final List<Player> playersGameList = new ArrayList<>();
    private final List<Player> playersServerList = new ArrayList<>();
    private final HashMap<Block, Location> blocksLocation = new HashMap<>();
    private final HashMap<Player, Integer> playersPoints = new HashMap<>();
    private final HashMap<Player, Material> playersBlock = new HashMap<>();
    private final int maxPlayer = 6;
    private String map = "Donjon de bois";


    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getServer().createWorld(new WorldCreator("DAC"));
        setGameState(GameState.WAITING);
        initListeners();
        this.playerTurn = new PlayerTurn();

    }

    @Override
    public void onDisable() {
        clearBlocks();
        instance = null;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public List<Player> getPlayersGameList() {
        return playersGameList;
    }

    public List<Player> getPlayersServerList() {
        return playersServerList;
    }

    public boolean isState(GameState gameState) {
        return (this.gameState == gameState);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public static DAC getInstance() {
        return instance;
    }

    public HashMap<Player, Integer> getPlayersPoints() {
        return playersPoints;
    }

    public HashMap<Block, Location> getBlocksLocation() {
        return blocksLocation;
    }

    public HashMap<Player, Material> getPlayersBlock() {
        return playersBlock;
    }

    private void initListeners(){
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new Damage(), this);
        getServer().getPluginManager().registerEvents(new FrameIntegrity(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerFall(), this);
        getServer().getPluginManager().registerEvents(new PlayerIntereact(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new FoodLevel(), this);
        getServer().getPluginManager().registerEvents(new CreatureSpawn(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
    }

    private void clearBlocks(){
        for(Location loc : getBlocksLocation().values()){
            loc.getBlock().setType(Material.WATER);
        }
        getBlocksLocation().clear();

    }

    public void setScorboardW(Player player) {
        if(RcbAPI.getInstance().boards.containsKey(player)){
            RcbAPI.getInstance().boards.get(player).setLine(1, "§b");
            RcbAPI.getInstance().boards.get(player).setLine(2, "§eBoosters actifs: §c" + 0 + "§cx §6+100%");
            RcbAPI.getInstance().boards.get(player).setLine(3, "§l  ");
            RcbAPI.getInstance().boards.get(player).setLine(4, "§6Map: §c" + map);
            RcbAPI.getInstance().boards.get(player).setLine(5, "§a ");
            RcbAPI.getInstance().boards.get(player).setLine(6, "§cEn attente de joueurs");
            RcbAPI.getInstance().boards.get(player).setLine(7, "§l");
            RcbAPI.getInstance().boards.get(player).setLine(8, "§7<" + getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
            RcbAPI.getInstance().boards.get(player).setLine(9, "§4      ");
            RcbAPI.getInstance().boards.get(player).setLine(10, "§eplay.rushcubeland.fr");
        }
        else
        {
            ScoreboardSign scoreboard = new ScoreboardSign(player, "§bDé §6à §bCoudre");
            scoreboard.create();
            scoreboard.setLine(1, "§b");
            scoreboard.setLine(2, "§eBoosters actifs: §c" + 0 + "§cx §6+100%");
            scoreboard.setLine(3, "§l  ");
            scoreboard.setLine(4, "§6Map: §c" + map);
            scoreboard.setLine(5, "§a ");
            scoreboard.setLine(6, "§cEn attente de joueurs");
            scoreboard.setLine(7, "§l");
            scoreboard.setLine(8, "§7<" + getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
            scoreboard.setLine(9, "§4      ");
            scoreboard.setLine(10, "§eplay.rushcubeland.fr");
            RcbAPI.getInstance().boards.put(player, scoreboard);
        }
    }

    public void setScorboardS(Player player) {
        if(RcbAPI.getInstance().boards.containsKey(player)){
            RcbAPI.getInstance().boards.get(player).setLine(1, "§b");
            RcbAPI.getInstance().boards.get(player).setLine(2, "§eBoosters actifs: §c" + 0 + "§cx §6+100%");
            RcbAPI.getInstance().boards.get(player).setLine(3, "§l  ");
            RcbAPI.getInstance().boards.get(player).setLine(4, "§6Map: §c" + map);
            RcbAPI.getInstance().boards.get(player).setLine(5, "§a ");
            RcbAPI.getInstance().boards.get(player).setLine(6, "§6Lancement dans: §e");
            RcbAPI.getInstance().boards.get(player).setLine(7, "§l");
            RcbAPI.getInstance().boards.get(player).setLine(8, "§7<" + getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
            RcbAPI.getInstance().boards.get(player).setLine(9, "§4      ");
            RcbAPI.getInstance().boards.get(player).setLine(10, "§eplay.rushcubeland.fr");
        }
        else
        {
            ScoreboardSign scoreboard = new ScoreboardSign(player, "§bDé §6à §bCoudre");
            scoreboard.create();
            scoreboard.setLine(1, "§b");
            scoreboard.setLine(2, "§eBoosters actifs: §c" + 0 + "§cx §6+100%");
            scoreboard.setLine(3, "§l  ");
            scoreboard.setLine(4, "§6Map: §c" + map);
            scoreboard.setLine(5, "§a ");
            scoreboard.setLine(6, "§6Lancement dans: §e");
            scoreboard.setLine(7, "§l");
            scoreboard.setLine(8, "§7<" + getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
            scoreboard.setLine(9, "§4      ");
            scoreboard.setLine(10, "§eplay.rushcubeland.fr");
            RcbAPI.getInstance().boards.put(player, scoreboard);
        }
    }

    public void setScorboardIP(Player player) {
        if(RcbAPI.getInstance().boards.containsKey(player)){
            RcbAPI.getInstance().boards.get(player).setLine(1, "§b");
            RcbAPI.getInstance().boards.get(player).setLine(2, "§6Map: §e" + map);
            RcbAPI.getInstance().boards.get(player).setLine(3, "§1 ");
            RcbAPI.getInstance().boards.get(player).setLine(4, "§6Tour: §e");
            RcbAPI.getInstance().boards.get(player).setLine(5, "§a ");
            RcbAPI.getInstance().boards.get(player).setLine(6, "§cPoints: §60");
            RcbAPI.getInstance().boards.get(player).setLine(7, "§l");
            RcbAPI.getInstance().boards.get(player).setLine(8, "§6Joueurs restant: §c" + getPlayersGameList().size());
            RcbAPI.getInstance().boards.get(player).setLine(9, "§4      ");
            RcbAPI.getInstance().boards.get(player).setLine(10, "§eplay.rushcubeland.fr");
        }
        else
        {
            ScoreboardSign scoreboard = new ScoreboardSign(player, "§bDé §6à §bCoudre");
            scoreboard.create();
            scoreboard.setLine(1, "§b");
            scoreboard.setLine(2, "§6Map: §e" + map);
            scoreboard.setLine(3, "§1 ");
            scoreboard.setLine(4, "§6Tour: §e");
            scoreboard.setLine(5, "§a ");
            scoreboard.setLine(6, "§cPoints: §60");
            scoreboard.setLine(7, "§l");
            scoreboard.setLine(8, "§6Joueurs restant: §c" + getPlayersGameList().size());
            scoreboard.setLine(9, "§4      ");
            scoreboard.setLine(10, "§eplay.rushcubeland.fr");
            RcbAPI.getInstance().boards.put(player, scoreboard);
        }
    }

    public PlayerTurn getPlayerTurn() {
        return playerTurn;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
