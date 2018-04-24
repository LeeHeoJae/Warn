package sabone;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.Config;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.EventHandler;

public class Warn extends PluginBase implements Listener{
		private Config warn;
		@Override
   public void onEnable() {
     this.getDataFolder().mkdirs();
     this.warn=new Config(this.getDataFolder()+"/warn.yml",Config.YAML);
     this.getLogger().info("활성화가 되었습니다.");
     this.getServer().getPluginManager().registerEvents(this,this);
   }
   @Override
   public void onDisable() {
   	  this.getLogger().info("비활성화가 되렀습니다.");
   	  this.warn.save();
   }
   
   @EventHandler
   public void onJoin(PlayerJoinEvent event){
   	 if(!this.warn.exists(event.getPlayer().getName().toLowerCase())){
   	 	 this.warn.set(event.getPlayer().getName().toLowerCase(),0);
   	 }
   }
   
   @Override
   public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
   	 if(cmd.getName().equalsIgnoreCase("경고")){
   	 	 if(!(sender instanceof Player)){
   	 	 	 sender.sendMessage("§o§4[경고] 플레이어만 가능합니다.");
   	 	 }
   	 	 else{
   	 	 	Player player=(Player)sender;
   	 	 	 if(args.length==0){
   	 	 	 	 player.sendMessage("§o§e[경고] /경고 <추가||빼기||내경고>");
   	 	 	 }
   	 	 	 else if(args[0].equals("추가")){
   	 	 	 	 if(!player.isOp()){
   	 	 	 	 	 player.sendMessage("§o§4[경고] 오피만 가능한 명령어 입니다.");
   	 	 	 	 }
   	 	 	 	 else if(args.length<2){
   	 	 	 	 	 player.sendMessage("§o§e[경고] /경고 <추가> [플레이어]");
   	 	 	 	 }
   	 	 	 	 else if(!this.warn.exists(args[1].toLowerCase())){
   	 	 	 	 	 player.sendMessage("§o§e[경고] 존재하지 않는 플레이어입니다.");
   	 	 	 	 }
   	 	 	 	 else{
   	 	 	 	 	 int i=Integer.parseInt(this.warn.get(args[1]).toString());
   	 	 	 	 	 this.warn.set(args[1],++i);
   	 	 	 	 	 player.sendMessage("§o§3[경고] 경고를 주었습니다.");
   	 	 	 	 	 if(i>=5){
   	 	 	 	  		 this.getServer().getNameBans().addBan(args[1]);
   	 	 	 	  		 this.getServer().getPlayer(args[1]).kick("§o§4"+ player.getName() + "에 의해 경고 5가 되어 밴됬습니다.");
   	 	  		 }
   	 	 	 	 }
   	 	 	 }
   	 	 	 else if(args[0].equals("빼기")){
   	 	 	 	 if(args.length<3){
   	 	 	 	  if(!player.isOp()){
   	 	 	 	  	player.sendMessage("§o§4[경고] 오피만 가능한 명령어 입니다.");
    	 	 	  }
   	 	 	   else if(args.length<2){
   	 	 	  	 player.sendMessage("§o§e[경고] /경고 <빼기> [플레이어 이름]");
   	 	 	   }
   	 	 	   else if(!this.warn.exists(args[1])){
   	 	 	  	 player.sendMessage("§o§e[경고] 존재하지 않는 플레이어입니다.");
   	 	 	   }
   	 	  	  else{
   	 	 	 	   	int i=Integer.parseInt(this.warn.get(args[1]).toString());
   	 	 	 	  	this.warn.set(args[1],--i);
   	 	 	 	  	player.sendMessage("§o§3[경고] 경고를 뺏습니다.");
   	 	 	 	  }
   	 	 	  }
   	 	 	 }
   	 	 	 else if(args[0].equals("내경고")){
   	 	 	 	 if(args.length<2){
   	 	 	 	 	 player.sendMessage("§o§3[경고] 내경고 : "+this.warn.get(player.getName()));
   	 	 	 	 }
   	 	 	 }
   	  	}
   	 }
   	 return true;
   }
 }