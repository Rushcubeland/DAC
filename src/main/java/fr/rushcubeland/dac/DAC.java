package fr.rushcubeland.dac;

import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.game.Locations;
import fr.rushcubeland.dac.game.PlayerTurn;
import fr.rushcubeland.dac.listeners.*;
import fr.rushcubeland.dac.spells.Spell;
import fr.rushcubeland.dac.tasks.Game;
import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.map.MapGroup;
import fr.rushcubeland.rcbcore.bukkit.map.MapUnit;
import fr.rushcubeland.rcbcore.bukkit.network.ServerUnit;
import fr.rushcubeland.rcbcore.bukkit.tools.ScoreboardSign;
import fr.rushcubeland.rcbcore.bukkit.tools.WorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.SecureRandom;
import java.util.*;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class DAC extends JavaPlugin {

    private static DAC instance;

    private GameState gameState;

    private PlayerTurn playerTurn;
    private final List<Player> playersGameList = new ArrayList<>();

    private final List<Player> playersServerList = new ArrayList<>();

    private final Map<Block, Location> blocksLocation = new HashMap<>();

    private final Map<Player, Integer> playersPoints = new HashMap<>();

    private final HashMap<Player, Material> playersBlock = new HashMap<>();

    private final HashMap<Player, Spell> playersSpell = new HashMap<>();

    public static final String DAC_PREFIX = ChatColor.YELLOW + "[" + ChatColor.AQUA + "Dé " + ChatColor.GOLD + "à " + ChatColor.AQUA + "Coudre" + ChatColor.YELLOW + "]";

    private int maxPlayer;
    private MapUnit map = null;


    @Override
    public void onEnable() {
        instance = this;
        setGameState(GameState.WAITING);
        initListeners();
        this.playerTurn = new PlayerTurn();
        Optional<ServerUnit> optional = ServerUnit.getByPort(Bukkit.getPort());
        optional.ifPresent(serverUnit -> this.maxPlayer = serverUnit.getMaxPlayers());
        chooseMap();
    }

    @Override
    public void onDisable() {
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

    private void fillScoreboard(ScoreboardSign sc, List<String> lines){
        if(sc != null && lines != null){
            for(int i = 1; i <= lines.size(); i++){
                sc.setLine(i, lines.get(i-1));
            }
        }
    }

    public void setScoreboard(Player player, GameState currentState){
        String ipText = ChatColor.YELLOW + "play.rushcubeland.fr";
        String mapText = ChatColor.GOLD + "Map: " + ChatColor.RED + map.getName();
        String playersRemain = ChatColor.GOLD + "Joueurs restant: " + ChatColor.RED + getPlayersGameList().size();
        String currentSlots = ChatColor.GRAY + "<" + getPlayersGameList().size() + ChatColor.YELLOW + "/" + ChatColor.GOLD + DAC.getInstance().getMaxPlayer() + ChatColor.GRAY + ">";
        List<String> lines = null;
        ScoreboardSign sc;
        if(RcbAPI.getInstance().boards.containsKey(player)){
            sc = RcbAPI.getInstance().boards.get(player);
        }
        else
        {
            sc = new ScoreboardSign(player, ChatColor.AQUA + "Dé " + ChatColor.GOLD + "à " + ChatColor.AQUA + "Coudre");
            sc.create();
            RcbAPI.getInstance().boards.put(player, sc);
        }
        switch (currentState) {
            case WAITING ->
                    lines = Arrays.asList("§1 ", ChatColor.YELLOW + "Boosters actifs: " + ChatColor.RED + 0 + ChatColor.RED + "x " + ChatColor.GOLD + "+100%",
                            "§c ",
                            mapText,
                            "§4 ",
                            ChatColor.RED + "En attente de joueurs...",
                            "§6 ",
                            currentSlots,
                            "§2 ",
                            ipText);
            case STARTING ->
                    lines = Arrays.asList("§c ", ChatColor.YELLOW + "Boosters actifs: " + ChatColor.RED + 0 + ChatColor.RED + "x " + ChatColor.GOLD + "+100%",
                            "§6 ",
                            mapText,
                            "§b ",
                            ChatColor.GOLD + "Lancement dans: " + ChatColor.YELLOW,
                            "§2 ",
                            currentSlots,
                            "§5 ",
                            ipText);
            case INPROGRESS -> lines = Arrays.asList("§6 ",
                    mapText,
                    "§b ",
                    ChatColor.GOLD + "Tour: " + ChatColor.YELLOW,
                    "§4 ",
                    ChatColor.RED + "Points: " + ChatColor.GOLD,
                    "§2 ",
                    playersRemain,
                    "§a ",
                    ipText);
        }
        fillScoreboard(sc, lines);
    }

    public void deathMethod(Player player){
        RcbAPI.getInstance().getAccountStatsDAC(player, result -> {
            AStatsDAC aStatsDAC = (AStatsDAC) result;
            aStatsDAC.setLoses(aStatsDAC.getLoses()+1);
            aStatsDAC.setNbJumps(aStatsDAC.getNbJumps()+1);
            aStatsDAC.setNbFailJumps(aStatsDAC.getNbFailJumps()+1);
            RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
            player.sendTitle(ChatColor.RED + "Dommage, tu t'es loupé !", ChatColor.WHITE + "Tu feras mieux la prochaine fois", 10, 70, 20);
            player.setGameMode(GameMode.SPECTATOR);
            player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0L, 0L);
            DAC.getInstance().getPlayersGameList().remove(player);
            for (Player pls : DAC.getInstance().getPlayersGameList()){
                pls.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.RED + " a hurté un bloc !");
            }
            player.teleport(Locations.POOL.getLocation());
            if(DAC.getInstance().getPlayersGameList().size() == 1) {
                DAC.getInstance().setGameState(GameState.FINISH);
                return;
            }
            DAC.getInstance().getPlayerTurn().initNextPlayer(DAC.getInstance().getPlayerTurn().getNextPositionRequired());
            player.getInventory().clear();
            Game.giveItems(player);
        });
    }

    private void chooseMap(){
        ArrayList<MapUnit> mapUnits = new ArrayList<>();
        for(MapUnit mapUnit : MapUnit.values()){
            if(mapUnit.getMapGroup().equals(MapGroup.DAC_POOL)){
                mapUnits.add(mapUnit);
            }
        }
        final int randInt = new SecureRandom().nextInt(mapUnits.size());
        setMap(mapUnits.get(randInt));
        WorldManager.replaceWorld(map.getPath(), true);
        Bukkit.getServer().createWorld(new WorldCreator(getMap().getPath()));
        Locations.POOL.getLocation().setWorld(Bukkit.getWorld(getMap().getPath()));
        Locations.DIVING_PLATFORM.getLocation().setWorld(Bukkit.getWorld(getMap().getPath()));
    }

    public PlayerTurn getPlayerTurn() {
        return playerTurn;
    }

    public MapUnit getMap() {
        return map;
    }

    public void setMap(MapUnit map) {
        this.map = map;
    }

    public HashMap<Player, Spell> getPlayersSpell() {
        return playersSpell;
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

    public Map<Player, Integer> getPlayersPoints() {
        return playersPoints;
    }

    public Map<Block, Location> getBlocksLocation() {
        return blocksLocation;
    }

    public Map<Player, Material> getPlayersBlock() {
        return playersBlock;
    }
}
