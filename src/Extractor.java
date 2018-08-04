import java.util.*;
import java.io.*;

class Extractor {
  String[] name_kwords;
  String[] place_kwords;
  String[] time_kwords;
  String[] time_rwords;
  String[] place_rwords;

  public Extractor(){
    name_kwords = File.getContentAsArray("name_kwords.txt");
    place_kwords = File.getContentAsArray("place_kwords.txt");
    time_kwords = File.getContentAsArray("time_kwords.txt");
    time_rwords = File.getContentAsArray("time_rwords.txt");
    place_rwords = File.getContentAsArray("place_rwords.txt");
  }

  public ArrayList<Entity> extract(String input){
    ArrayList<Entity> entities = new ArrayList<>();
    String curr_entity = "";
    String found_keyword = null;
    String found_kword = "";
    input = input.replace("\n", " ");
    String splitted[] = input.split(" ");

    for(int i = 0; i < splitted.length; i++){
    boolean name = false;
    boolean date = false;
    boolean place = false;
      /*
      Looks for Name signifiers
      */
      if(Arrays.asList(name_kwords).contains(splitted[i].toLowerCase().trim())){
        curr_entity = "PER";
        found_keyword = splitted[i].toLowerCase();
      }
    
      /*
      Looks for Place signifiers
      */
      if(Arrays.asList(place_kwords).contains(splitted[i].toLowerCase().trim())){
        curr_entity = "LOC";
        found_keyword = splitted[i].toLowerCase();
      }

      if(splitted[i].toLowerCase().equals("ang")){
        i++;
        String p_phrase = "";
        int k = i;
        boolean stop = false;
        if(splitted[i].toLowerCase().equals("organisasyon")){
          stop = true;
          Entity entity = new Entity();
          entity.setType("ORG");
          entity.setName(splitted[i]);
          entities.add(entity);
          curr_entity = "ORG";
          found_keyword = splitted[i].toLowerCase();
          place = true;
        }
        for(int j = i; j < splitted.length && !stop; j++){
          if(splitted[j].charAt(0) >= 'A' && splitted[j].charAt(0) <= 'Z'){

            place = true;
            if(splitted[j].indexOf(".") != -1){
              System.out.println(splitted[j]);
              k++;
              p_phrase = p_phrase + " " + splitted[j].substring(0, (splitted[j]).length()-1);
              if(Arrays.asList(place_rwords).contains(p_phrase.toLowerCase().trim())){
                Entity entity = new Entity();
                entity.setType("LOC");
                entity.setName(p_phrase);
                entities.add(entity);
              }
              stop = true;
            } else if(splitted[j].toLowerCase().indexOf(",") != -1){
              System.out.println(splitted[j]);
              splitted[j] = splitted[j].substring(0, (splitted[j].toLowerCase()).length()-1);
              k++;
              p_phrase += splitted[j] + " ";
              if(Arrays.asList(place_rwords).contains(p_phrase.toLowerCase().trim())){
                Entity entity = new Entity();
                entity.setType("LOC");
                entity.setName(p_phrase);
                entities.add(entity);
              }
              p_phrase = "";
            } else {
              k++;
               p_phrase = p_phrase + splitted[j] + " ";
            }  
          } else {
            if(splitted[j].toLowerCase().equals("del")){
              k++;
              p_phrase = p_phrase + " " + splitted[j];
            } else {
              // System.out.println(splitted[j]);
               if(splitted[j].toLowerCase().equals("ug") || splitted[j].toLowerCase().equals("og")){
                k++; 
               //System.out.println(p_phrase);
                if(Arrays.asList(place_rwords).contains(p_phrase.toLowerCase().trim())){
                  Entity entity = new Entity();
                  entity.setType("LOC");
                  entity.setName(p_phrase);
                  entities.add(entity); 
                }
                p_phrase = "";
                stop = true;
               } else {
                 if(!p_phrase.isEmpty()){
                  if(Arrays.asList(place_rwords).contains(p_phrase.toLowerCase().trim())){
                    Entity entity = new Entity();
                    entity.setType("LOC");
                    entity.setName(p_phrase);
                    entities.add(entity);
                  }
                  p_phrase = "";
                 }
                 //System.out.println(p_phrase);
                 stop = true;
               } 
              
            }
          }
        }
        //System.out.println(p_phrase);
      }


      /*
      Looks for Time signifiers
      */

      for(String t_kword : time_kwords){
        if(t_kword.equals(splitted[i].toLowerCase()) || splitted[i].toLowerCase().contains(t_kword)){
          curr_entity = "DATE";
          found_keyword = t_kword;
          if(t_kword.equals("ika-")){
            Entity entity = new Entity();
            entity.setType("TIME");
            entity.setName(splitted[i]);
            entities.add(entity);
            date = true;
          }
          break;
        } 
      }

      /*
      Looks for Reserved Words (Date & Time)
      */
      boolean found = false;
      String before_str = "";
      boolean done = false;
      for(String t_rword : time_rwords){
        if(splitted[i].toLowerCase().contains(".")){
         before_str = splitted[i];
        }
        if((splitted[i].toLowerCase()).contains(".") || (splitted[i].toLowerCase()).contains(",")){
          splitted[i] = splitted[i].substring(0, (splitted[i].toLowerCase()).length()-1);
        }

        if(t_rword.equals(splitted[i].toLowerCase().trim())){
          
          if(t_rword.equals("ala") || t_rword.equals("alas")){
            Entity entity = new Entity();
            entity.setType("TIME");
            if((splitted[i+1].toLowerCase()).contains(".") || (splitted[i+1].toLowerCase()).contains(",")){
             splitted[i+1] = splitted[i+1].substring(0, (splitted[i+1].toLowerCase()).length()-1);
            }
            entity.setName(splitted[i] + " " + splitted[i+1]);
            entities.add(entity);
            done = true;
          }
          String st = t_rword;
          if(st.equals("enero") || st.equals("pebrero") || st.equals("marso") || st.equals("abril") ||
             st.equals("mayo")  || st.equals("hunyo")   || st.equals("agosto")|| st.equals("septiyembre") ||
             st.equals("nubiyembre") || st.equals("disyembre")){
            int s = i;
            if(s + 2 < splitted.length){
              if(splitted[s+1].trim().matches(".*\\d+.*") && splitted[s+2].trim().matches(".*\\d+.*")){
                Entity entity = new Entity();
                entity.setType("TIME");
                if((splitted[s+1].toLowerCase()).contains(".")){
                 splitted[s+1] = splitted[s+1].substring(0, (splitted[s+1].toLowerCase()).length()-1);
                }
                entity.setName(splitted[i] + " " + splitted[s + 1] + " " + splitted[s + 2]);
                entities.add(entity);
                done = true;
              }
            } else if(s + 1 < splitted.length){
              if((splitted[i+1].toLowerCase()).contains(".")){
               splitted[i+1] = splitted[i+1].substring(0, (splitted[i+1].toLowerCase()).length()-1);
              }
              Entity entity = new Entity();
              entity.setType("TIME");
              entity.setName(splitted[i] + " " + splitted[i + 1]);
              entities.add(entity);
          
              done = true;
            }
          }
          if(st.equals("am") || st.equals("pm")){
            
            if((splitted[i - 1].toLowerCase()).contains(".")){
              splitted[i - 1] = splitted[i - 1].substring(0, (splitted[i - 1].toLowerCase()).length()-1);
            }
            Entity entity = new Entity();
            entity.setType("TIME");
            entity.setName(splitted[i - 1] + " " + splitted[i]);
            entities.add(entity);
            done = true;
          } if(!done){
            Entity entity = new Entity();
            entity.setType("TIME");
            entity.setName(splitted[i]);
            entities.add(entity);
          }

          found = true;
          date = true;
          break;
        } 

      }



      /*
      Looks for Proper Nouns in Big Letters
      */
      if(splitted[i].charAt(0) >= 'A' && splitted[i].charAt(0) <= 'Z' && !place && curr_entity != null && !date){
        if(found_keyword != null && !found_keyword.toLowerCase().equals(splitted[i].toLowerCase()) && !found){
          Entity entity = new Entity();
          entity.setType(curr_entity);
          entity.setName(splitted[i]);
          entities.add(entity);
          if(before_str.contains(".")){
            curr_entity = null;
          }
        } 
      } else {
        if(splitted[i].toLowerCase().equals("del")){
          Entity entity = new Entity();
          entity.setType(curr_entity);
          entity.setName(splitted[i]);
          entities.add(entity);
        }
          if(before_str.contains(".")){
            curr_entity = null;
          }
      } 
    }
    return entities;
  }

	public static void main(String[] args) {
		String input = "Ang mga tawo sa UP Cebu nga mga naangol sa pagbomba. Naapil silang Justine Navaja, Arvin Arbuis sa mga naangol. Nahitabo ang panghitabo niadtong Pebrero 2, 2016 sa alas dos sa hapon.";
    Extractor e = new Extractor();
    ArrayList<Entity> entities = new ArrayList<>();
    entities = e.extract(input);  
    

    for(Entity entity : entities){
      System.out.println(entity.name + " " + entity.type);
    }
	}

	public boolean checkTime(String word){
   return false;
	}
}

