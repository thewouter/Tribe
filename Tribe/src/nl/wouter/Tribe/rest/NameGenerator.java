package nl.wouter.Tribe.rest;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.sun.xml.internal.ws.util.StringUtils;

public class NameGenerator {
	private String[] consonants, vowels;
	private Random random;
	
	public NameGenerator(){
		Scanner scanner;
		random = new Random();
		
		try{
			scanner = new Scanner(this.getClass().getResourceAsStream("/res/vowels.txt"));
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		if(!scanner.nextLine().equals("vowels:")){
			System.out.println("wrong file!");
			return;
		}
		
		ArrayList<String> vowelList = new ArrayList<String>();
		ArrayList<String> consonantList = new ArrayList<String>();
		
		boolean addToVowelList = true;
		while(scanner.hasNext()){
			String line = scanner.next();
			
			if(line.equals("consonants:")){
				addToVowelList = false;
				continue;
			}
			
			if(addToVowelList) vowelList.add(line);
			else consonantList.add(line);
		}
		
		consonants = toArray(consonantList);
		vowels = toArray(vowelList);
	}
	
	public String getRandomName(int size){
		String result = "";
		
		int startWithVowel = random.nextBoolean() ? 1 : 0;
		
		for(int i = 0; i < size; i++){
			if(i % 2 == startWithVowel) result += getRandomString(consonants);
			else result += getRandomString(vowels);
		}
		
		return StringUtils.capitalize(result);
	}
	
	public String getRandomName(){
		return getRandomName(3 + random.nextInt(5));
	}
	
	private String getRandomString(String[] array){
		return array[random.nextInt(array.length)];
	}
	
	private String[] toArray(ArrayList<String> list){
		String[] result = new String[list.size()];
		
		for(int i = 0; i < list.size(); i++){
			result[i] = list.get(i);
		}
		
		return result;
	}
	
	public static void main(String[] args){
		NameGenerator gen = new NameGenerator();
		
		for(int i = 0; i < 20; i++){
			System.out.println(gen.getRandomName());
		}
	}
}
