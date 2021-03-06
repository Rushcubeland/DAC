package fr.didi955.dac;

import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.didi955.dac.game.PlayerTurn;
import fr.didi955.dac.listeners.*;
import fr.didi955.dac.spells.Spell;
import fr.didi955.dac.tasks.Game;
import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

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
    private final ArrayList<Player> playersGameList = new ArrayList<>();
    private final ArrayList<Player> playersServerList = new ArrayList<>();
    private final HashMap<Block, Location> blocksLocation = new HashMap<>();
    private final HashMap<Player, Integer> playersPoints = new HashMap<>();
    private final HashMap<Player, Material> playersBlock = new HashMap<>();
    private final HashMap<Player, Spell> playersSpell = new HashMap<>();
    Optional<ServerUnit> optional = ServerUnit.getByPort(Bukkit.getPort());
    private int maxPlayer;
    private MapUnit map = null;


    @Override
    public void onEnable() {
        instance = this;
        setGameState(GameState.WAITING);
        initListeners();
        this.playerTurn = new PlayerTurn();
        optional.ifPresent(serverUnit -> this.maxPlayer = serverUnit.getMaxPlayers());
        chooseMap();
    }

    @Override
    public void onDisable() {
        WorldManager.replaceWorld(map.getPath(), true);
        instance = null;
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

    public void setScorboardW(Player player) {
        if(RcbAPI.getInstance().boards.containsKey(player)){
            ScoreboardSign sc = RcbAPI.getInstance().boards.get(player);
            sc.setLine(1, "??b");
            sc.setLine(2, "??eBoosters actifs: ??c" + 0 + "??cx ??6+100%");
            sc.setLine(3, "??l  ");
            sc.setLine(4, "??6Map: ??c" + map.getName());
            sc.setLine(5, "??a ");
            sc.setLine(6, "??cEn attente de joueurs");
            sc.setLine(7, "??l");
            sc.setLine(8, "??7<" + getPlayersGameList().size() + "??e/??6" + DAC.getInstance().getMaxPlayer() + "??7>");
            sc.setLine(9, "??4      ");
            sc.setLine(10, "??eplay.rushcubeland.fr");
        }
        else
        {
            ScoreboardSign scoreboard = new ScoreboardSign(player, "??bD?? ??6?? ??bCoudre");
            scoreboard.create();
            scoreboard.setLine(1, "??b");
            scoreboard.setLine(2, "??eBoosters actifs: ??c" + 0 + "??cx ??6+100%");
            scoreboard.setLine(3, "??l  ");
            scoreboard.setLine(4, "??6Map: ??c" + map.getName());
            scoreboard.setLine(5, "??a ");
            scoreboard.setLine(6, "??cEn attente de joueurs");
            scoreboard.setLine(7, "??l");
            scoreboard.setLine(8, "??7<" + getPlayersGameList().size() + "??e/??6" + DAC.getInstance().getMaxPlayer() + "??7>");
            scoreboard.setLine(9, "??4      ");
            scoreboard.setLine(10, "??eplay.rushcubeland.fr");
            RcbAPI.getInstance().boards.put(player, scoreboard);
        }
    }

    public void setScorboardS(Player player) {
        if(RcbAPI.getInstance().boards.containsKey(player)){
            ScoreboardSign sc = RcbAPI.getInstance().boards.get(player);
            sc.setLine(1, "??b");
            sc.setLine(2, "??eBoosters actifs: ??c" + 0 + "??cx ??6+100%");
            sc.setLine(3, "??l  ");
            sc.setLine(4, "??6Map: ??c" + map.getName());
            sc.setLine(5, "??a ");
            sc.setLine(6, "??6Lancement dans: ??e");
            sc.setLine(7, "??l");
            sc.setLine(8, "??7<" + getPlayersGameList().size() + "??e/??6" + DAC.getInstance().getMaxPlayer() + "??7>");
            sc.setLine(9, "??4      ");
            sc.setLine(10, "??eplay.rushcubeland.fr");
        }
        else
        {
            ScoreboardSign scoreboard = new ScoreboardSign(player, "??bD?? ??6?? ??bCoudre");
            scoreboard.create();
            scoreboard.setLine(1, "??b");
            scoreboard.setLine(2, "??eBoosters actifs: ??c" + 0 + "??cx ??6+100%");
            scoreboard.setLine(3, "??l  ");
            scoreboard.setLine(4, "??6Map: ??c" + map.getName());
            scoreboard.setLine(5, "??a ");
            scoreboard.setLine(6, "??6Lancement dans: ??e");
            scoreboard.setLine(7, "??l");
            scoreboard.setLine(8, "??7<" + getPlayersGameList().size() + "??e/??6" + DAC.getInstance().getMaxPlayer() + "??7>");
            scoreboard.setLine(9, "??4      ");
            scoreboard.setLine(10, "??eplay.rushcubeland.fr");
            RcbAPI.getInstance().boards.put(player, scoreboard);
        }
    }

    public void setScorboardIP(Player player) {
        if(RcbAPI.getInstance().boards.containsKey(player)){
            ScoreboardSign sc = RcbAPI.getInstance().boards.get(player);
            sc.setLine(1, "??b");
            sc.setLine(2, "??6Map: ??e" + map.getName());
            sc.setLine(3, "??1 ");
            sc.setLine(4, "??6Tour: ??e");
            sc.setLine(5, "??a ");
            sc.setLine(6, "??cPoints: ??60");
            sc.setLine(7, "??l");
            sc.setLine(8, "??6Joueurs restant: ??c" + getPlayersGameList().size());
            sc.setLine(9, "??4      ");
            sc.setLine(10, "??eplay.rushcubeland.fr");
        }
        else
        {
            ScoreboardSign scoreboard = new ScoreboardSign(player, "??bD?? ??6?? ??bCoudre");
            scoreboard.create();
            scoreboard.setLine(1, "??b");
            scoreboard.setLine(2, "??6Map: ??e" + map.getName());
            scoreboard.setLine(3, "??1 ");
            scoreboard.setLine(4, "??6Tour: ??e");
            scoreboard.setLine(5, "??a ");
            scoreboard.setLine(6, "??cPoints: ??60");
            scoreboard.setLine(7, "??l");
            scoreboard.setLine(8, "??6Joueurs restant: ??c" + getPlayersGameList().size());
            scoreboard.setLine(9, "??4      ");
            scoreboard.setLine(10, "??eplay.rushcubeland.fr");
            RcbAPI.getInstance().boards.put(player, scoreboard);
        }
    }

    public void deathMethod(Player player){
        RcbAPI.getInstance().getAccountStatsDAC(player, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                AStatsDAC aStatsDAC = (AStatsDAC) result;
                aStatsDAC.setLoses(aStatsDAC.getLoses()+1);
                aStatsDAC.setNbJumps(aStatsDAC.getNbJumps()+1);
                aStatsDAC.setNbFailJumps(aStatsDAC.getNbFailJumps()+1);
                RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
                player.sendTitle("??cDommage, tu t'es loup?? !", "??fTu feras mieux la prochaine fois", 10, 70, 20);
                player.setGameMode(GameMode.SPECTATOR);
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0L, 0L);
                DAC.getInstance().getPlayersGameList().remove(player);
                for (Player pls : DAC.getInstance().getPlayersGameList()){
                    pls.sendMessage("??e" + player.getDisplayName() + " ??ca hurt?? un bloc !");
                }
                player.teleport(Locations.POOL.getLocation());
                if(DAC.getInstance().getPlayersGameList().size() == 1) {
                    DAC.getInstance().setGameState(GameState.FINISH);
                    return;
                }
                DAC.getInstance().getPlayerTurn().initNextPlayer(DAC.getInstance().getPlayerTurn().getNextPositionRequired());
                player.getInventory().clear();
                Game.giveItems(player);
            }
        });
    }

    private void chooseMap(){
        ArrayList<MapUnit> mapUnits = new ArrayList<>();
        for(MapUnit mapUnit : MapUnit.values()){
            if(mapUnit.getMapGroup().equals(MapGroup.DAC_POOL)){
                mapUnits.add(mapUnit);
            }
        }
        final int randInt = new Random().nextInt(mapUnits.size());
        setMap(mapUnits.get(randInt));
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

    public ArrayList<Player> getPlayersGameList() {
        return playersGameList;
    }

    public ArrayList<Player> getPlayersServerList() {
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
}
