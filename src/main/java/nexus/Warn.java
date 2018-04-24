package nexus;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import cn.nukkit.form.window.*;
import cn.nukkit.form.response.*;
import cn.nukkit.form.element.*;
import cn.nukkit.utils.Config;

import nexus.form.WarnForm;

import java.util.ArrayList;

public class Warn extends PluginBase implements Listener{
	Config config;
	static Warn instance;
	public static Warn getInstance(){
		return instance;
	}
	@Override
	public void onEnable(){
		this.getDataFolder().mkdirs();
		this.config=new Config(this.getDataFolder()+"/warn.json",Config.JSON);
		this.getServer().getPluginManager().registerEvents(this,this);
		this.instance=this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
		if(sender instanceof Player){
			Player player=(Player) sender;
			if(!player.isOp()){
				player.showFormWindow(new FormWindowSimple("경고 횟수","당신의 경고 횟수는 "+this.getNumber(player)+"회 입니다."));
				return true;
			}
			ArrayList<String> playerList=new ArrayList<String>();
			for(Player p:this.getServer().getOnlinePlayers().values()){
				if(!p.isOp()) playerList.add(p.getName());
			}
			WarnForm window=new WarnForm();
			ElementDropdown dropDown=new ElementDropdown("대상",playerList);
			ElementToggle toggle=new ElementToggle("경고 부여",true);
			ElementSlider slider=new ElementSlider("횟수",1,3,1,1);
			ElementInput input=new ElementInput("사유","사유를 입력해주세요","관리진의 재량");
			
			window.addElement(dropDown);
			window.addElement(toggle);
			window.addElement(slider);
			window.addElement(input);
			player.showFormWindow(window);
		}
		return true;
	}
	
	@EventHandler
	public void onSend(PlayerFormRespondedEvent ev){
		if(ev.getWindow() instanceof WarnForm){
			if(ev.getResponse() instanceof FormResponseCustom){
				FormResponseCustom result=(FormResponseCustom)ev.getResponse();
				FormResponseData dropDownData=result.getDropdownResponse(0);
				String dropDown=dropDownData.getElementContent();
				boolean toggle=result.getToggleResponse(1);
				int slider=(int)result.getSliderResponse(2);
				String input=result.getInputResponse(3);
				Player target=this.getServer().getPlayer(dropDown);
				if(target==null){
					ev.getPlayer().showFormWindow(new FormWindowSimple("Sorry","대상이 잘못되었습니다."));
					return;
				}
				if(input==null||input.isEmpty()){
					ev.getPlayer().showFormWindow(new FormWindowSimple("Sorry","사유를 입력해주세요."));
				}
				this.addNumber(target,slider);
				if(!toggle){
					target.sendTip("§4[경고] "+ev.getPlayer().getName()+"님에 의해 경고가 "+slider+"맘큼 줄어들었습니다.\n§4[경고] 사유 : "+input+"\n§4[경고] 누적된 경고 수 : "+this.getNumber(ev.getPlayer()));
					return;
				}
				target.sendTip("§4[경고] "+ev.getPlayer().getName()+"님에 의해 경고를 "+slider+"맘큼 받았습니다.\n§4[경고] 사유 : "+input+"\n§4[경고] 누적된 경고 수 : "+this.getNumber(ev.getPlayer()));
				if(this.isOut(target.getName())){
					this.getServer().getNameBans().addBan(target.getName(),"경고 3회 누적");
					this.getServer().broadcastMessage("§7"+target.getName()+"님이 경고 3회 누적으로 금지되었습니다.");
				}
				if(target!=null){
					target.kick(null,"경고 3회 누적");
				}
			}
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent ev){
		if(!this.config.exists(ev.getPlayer().getName())){
			this.config.set(ev.getPlayer().getName(),0);
		}
	}
	
	public boolean isOut(String player){
		if(this.config.getInt(player)>=3){
			return true;
		}
		return false;
	}
	public int getNumber(Player player){
		return this.getNumber(player.getName());
	}
	public int getNumber(String player){
		if(!this.config.exists(player)) return 0;
		return this.config.getInt(player);
	}
	public void addNumber(Player player,int number){
		this.addNumber(player.getName(),number);
	}
	public void addNumber(String player,int number){
		if(this.config.exists(player)){
			this.config.set(player,getNumber(player)+number);
			return;
		}
		this.config.set(player,number);
	}
	public void setNumber(Player player,int number){
		this.addNumber(player.getName(),number);
	}
	public void setNumber(String player,int number){
		this.config.set(player,number);
	}
}