package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class Smoothie {

	public enum Menu {
		CLASSIC("Classic"),
		FREEZIE("Freezie"),
		GREENIE("Greenie"),
		DESSERTS("Just Desserts");

		private final String name;

		Menu(String name){
			this.name = name;
		}

		public String getName() {
			return this.name;
		}


		@Override
		public String toString() {
			return this.name;
		}
	}

	private static final Map<Menu, List<String>> menuMap = new ConcurrentHashMap<>();

	private static Map<Menu, List<String>> getMenuMap() {
		if (menuMap.isEmpty()) {
			menuMap.put(Menu.CLASSIC, new ArrayList<>(Arrays.asList("strawberry", "banana", "pineapple", "mango", "peach", "honey")));
			menuMap.put(Menu.FREEZIE, new ArrayList<>(Arrays.asList("blackberry", "blueberry", "black currant", "grape juice", "frozen yogurt")));
			menuMap.put(Menu.GREENIE, new ArrayList<>(Arrays.asList("green apple", "lime", "avocado", "spinach", "ice", "apple juice")));
			menuMap.put(Menu.DESSERTS, new ArrayList<>(Arrays.asList("banana", "ice cream", "chocolate", "peanut, cherry")));
		}
		return menuMap;
	}

	public static String ingredients(final String order) {
		final var instructions = Arrays.asList(order.split(","));
		List<String> menu = Arrays.stream(Menu.values()).map(Menu::getName).collect(Collectors.toList());
		final var menuList = String.join(",", menu);
		if (instructions.isEmpty()) {
			throw new IllegalArgumentException("There must be an order available!");
		}
		final var pickedOrder = findOrderByName(instructions.get(0));
		if (pickedOrder == null) {
			throw new IllegalArgumentException(String.format("The order must be one of Menu items %s", menuList));
		}
		final var allergies = instructions.subList(1, instructions.size()).stream()
				.filter(value -> value.startsWith("-"))
				.map(value -> value.substring(1))
				.toList();
		final var recipe = getMenuMap().get(pickedOrder);
		recipe.removeAll(allergies);
		recipe.sort(String::compareToIgnoreCase);
		if (recipe.isEmpty()) {
			return "";
		}
		return String.join(",", recipe);
	}

	public static Menu findOrderByName(String name) {
		Menu result = null;
		for (Menu menu : Menu.values()) {
			if (menu.getName().equalsIgnoreCase(name)) {
				result = menu;
				break;
			}
		}
		return result;
	}
}
