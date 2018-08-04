import java.util.*;
public class Entity {
 public String type;
 public String name;
 public int index;

 public Entity(){
 	 type = "";
 	 name = "";
   index = -1;
 }
 public void setType(String type){
 	this.type = type;
 }
 public void setName(String name){
 	 this.name += name;
 }
 public void setIdx(int i){
 	 this.index = i;
 }
}