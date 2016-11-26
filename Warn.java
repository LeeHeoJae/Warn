package sabone;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.Config;
import cn.nukkut.player.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class Warn extends PluginBase implements Listener{
		public Config config;
		public Config warn;
		@Override
   public void onEnable() {
     this.getServer().getPluginManager().registerEvensts(this,this);
     this.getDataFolder().mkdirs();
     this.config=new Config(this.getDataFolder()+"/config.yml",Config.YAML);
     this.warn=new Config(this.getDataFolder()+"/warn.yml",Config.YAML);
     if(!config.get("ban-count")){
     	config.set("ban-count",5);
     }
     this.getLogger().info("[경고] 활성화가 되었습니다.");
   }
   @Override
   public void onDisable() {
   	  this.getLogger().info("[경고] 비활성화가 되렀습니다.");
   	  config.save();
   	  warn.save();
   }
   
   public int getCount(String player){
   	  return warn.get(player);
   }
   
   public void CountFive(Player player){
   	  if(warn.get(player)>=config.get("ban-count")){
   	  	server.getNameBans().addBan(player);
   	  }
   }
   public void addCount(String player,int count,Player sender){
   	  warn.set(player.getName(),warn.get(player.getName())+count);
   	  this.CountFive(sender);
   }
   public void addCount(String player,float counts,Player sender){
     int counts=(float)count;
     warn.set(player.getName(),warn.get(player.getName())+count);
     this.CountFive(sender);
   }
   public void addCount(String player,char count,Player sender){
   	  player.sendMessage("[경고] 줄 경고의 개수를 숫자로 적어주세요.");
   }
   public void setCount(String player,int count,Player sender){
   	  if(warn.get(player)>=config.get("ban-count")){
   	  	server.getNameBans().addBan(player);
   	  	return true;
   	  }
   	  warn.set(player,count);
   }
   public void setCount(String player,float counts,Player sender){
   	  int counts=(float)count
   	  if(warn.get(player)>=config.get("ban-count")){
   	  	server.getNameBans().addBan(player);
   	  	return true;
   	  }
   	  warn.set(player,count);
   }
   public void setCount(String player,char counts,Player sender){
   	  sender.sendMessage("[경고] 경고수를 숫자로 적어주세요.");
   }
   @Override
   public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
   	  Player player = (Player)sender;
   	  if(cmd.equals("경고")){
   	  	switch(args[0]){
   	  		case "내경고": case "list": case "l":
   	  		if(player.hasPermission("me.sabone.warn.list")){
   	  			warn.get(sender);
   	  			sender.sendMessage("[경고] 내 경고 수 : "+warn.get(sender));
   	  		}
   	  		case "설정": case "set": case "s":
   	  		if(player.hasPermission("me.sabone.warn.set")){
   	  			if(!player.isOp()){
   	  				player.sendMessage("[경고] 오피만 가능한 명령어 입니다.");
   	  				return true;
   	  			}
   	  			if((args[1]==null||args[1].equals(" "))||(args[2]==null||args[2].equals(" "))){
   	  				player.sendMessage("[경고] /경고 설정(S) [플레이어] [수]");
   	  				return true;
   	  			}
   	  			this.setCount(args[1],args[2],sender);
   	  			sender.sendMessage("[경고] "+args[1]+"님의 경고 : "+this.getCount(args[1]));
   	  		}
   	  		case "추가": case "a": case "add":
   	  		if(player.hasPermission("me.sabone.warn.add")){
   	  			if(!player.isOp()){
   	  				player.sendMessage("[경고] 오피만 가능한 명령어 입니다.");
   	  				return true;
   	  			}
   	  			if((args[1].equals(" ")||args[1]==null)||(args[2].equals(" ")||args[2]==null)){
   	  				player.sendMessage("[경고] /경고 추가(A) [플레이어] [개수]");
   	  				return true;
   	  			}
   	  			this.addCount(args[1],args[2],player);
   	  			player.sendMessage("[경고] "+args[1]+"님의 경고 : "+this.getCount(args[1]));
   	  			return true;
   	  		}
   	  	}
   }
}