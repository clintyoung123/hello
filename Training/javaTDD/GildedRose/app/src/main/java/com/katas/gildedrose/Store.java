package com.katas.gildedrose;

/*
Hi and welcome to team Gilded Rose. As you know, we are a small inn with a prime location in a prominent city ran by a friendly innkeeper named Allison
We also buy and sell only the finest goods. Unfortunately, our goods are constantly degrading in quality as they approach their sell by date.
We have a system in place that updates our inventory for us. It was developed by a no-nonsense type named Leeroy, who has moved on to new adventures.
Your task is to add the new feature to our system so that we can begin selling a new category of items. First an introduction to our system:

	- All items have a SellIn value which denotes the number of days we have to sell the item
	- All items have a Quality value which denotes how valuable the item is
	- At the end of each day our system lowers both values for every item

Pretty simple, right? Well this is where it gets interesting:

	- Once the sell by date has passed, Quality degrades twice as fast
	- The Quality of an item is never negative
	- "Aged Brie" actually increases in Quality the older it gets
	- The Quality of an item is never more than 50
	- "Sulfuras", being a legendary item, never has to be sold or decreases in Quality
	- "Backstage passes", like aged brie, increases in Quality as it's SellIn value approaches; Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or less but Quality drops to 0 after the concert

We have recently signed a supplier of conjured items. This requires an update to our system:

	- "Conjured" items degrade in Quality twice as fast as normal items

Feel free to make any changes to the UpdateQuality method and add any new code as long as everything still works correctly.

IMPORTANT!!
IMPORTANT!!
However, do not alter the Item class or Items property as those belong to the goblin in the corner who will insta-rage and one-shot you as he doesn't believe in shared code ownership.
IMPORTANT!!
IMPORTANT!!

Just for clarification, an item can never have its Quality increase above 50, however "Sulfuras" is a legendary item and as such its Quality is 80 and it never alters.
*/


import java.util.HashMap;
import java.util.Map;

class Item {

    public final String Name;
    private int sellIn;
    private int quality;

    public Item(String name, int sellIn, int quality) {
        this.Name = name;
        this.sellIn = sellIn;
        this.quality = quality;
    }

    public int getSellIn () { return sellIn; }
    public int getQuality() { return quality; }

    public void setSellIn (int sellIn)  { this.sellIn  = sellIn; }
    public void setQuality(int quality) { this.quality = quality; }
}

class Store {

    private Item[] items;

    public Item[] getItems() {
        return items;
    }

    public Store() {
        items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Elixir of the Mongoose", 5, 7),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Conjured Mana Cake", 3, 6)
        };
    }

    /*
        IMPORTANT!!
        IMPORTANT!!
        You may change code below here only!!!
        IMPORTANT!!
        IMPORTANT!!
    */
    public interface IItemUpdater {
        void update(Item item);
    }

    static class Updater {
        private final static Map<String, IItemUpdater> map = new HashMap<String, IItemUpdater>();

        public Updater() {

            map.put("Sulfuras, Hand of Ragnaros", new IItemUpdater() {
                @Override
                public void update(Item item) {
                }
            });

            map.put("Aged Brie", new IItemUpdater() {
                @Override
                public void update(Item item) {
                    if (item.getQuality() < 50) {
                        IncreaseQuality(item);
                    }
                    DecreaseSellInDays(item);
                    if (item.getSellIn() < 0) {
                        if (item.getQuality() < 50) {
                            IncreaseQuality(item);
                        }
                    }
                }
            });
            map.put("Backstage passes to a TAFKAL80ETC concert", new IItemUpdater() {
                @Override
                public void update(Item item) {
                    if (item.getQuality() < 50) {
                        IncreaseQuality(item);

                        if (item.getSellIn() < 11) {
                            IncreaseQuality(item);
                        }

                        if (item.getSellIn() < 6) {
                            IncreaseQuality(item);
                        }
                    }
                    DecreaseSellInDays(item);

                    if (item.getSellIn() < 0) {
                        item.setQuality(0);
                    }
                }
            });
            //Default Updater
            //            map.put("Elixir of the Mongoose", new IUpdater() {
            //            map.put("Conjured Mana Cake", new IUpdater() {
            //            map.put("+5 Dexterity Vest", new IUpdater() {
            map.put("defaultUpdater", new IItemUpdater() {
                @Override
                public void update(Item item) {
                    if (item.getQuality() > 0) {
                        DecreaseQuality(item);
                    }
                    DecreaseSellInDays(item);
                    if (item.getSellIn() < 0) {
                        if (item.getQuality() > 0) {
                            DecreaseQuality(item);
                        }
                    }
                }
            });
        }

        public void updateItem(Item item){
            IItemUpdater itemUpdater = map.get(item.Name);
            if (itemUpdater!=null){
                itemUpdater.update(item);
            }else{
                map.get("defaultUpdater").update(item);
            }
        }


        private void DecreaseQuality(Item item) {
            item.setQuality(item.getQuality() - 1);
        }

        private void IncreaseQuality(Item item) {
            item.setQuality(item.getQuality() + 1);
        }

        private void DecreaseSellInDays(Item item) {
            item.setSellIn(item.getSellIn() - 1);
        }

    }

    public void updateQuality(){
        Updater updater = new Updater();
        for (Item item:items){
//            if (item.Name == "Sulfuras, Hand of Ragnaros") {
                updater.updateItem(item);
//            }else {
//                updateQuality1(item);
//            }
        }
    }

    public void updateQuality1(Item item) {
//        for (int i = 0; i < items.length; i++) {
//            Item item = items[i];
            String name = item.Name;
            if (name == "Sulfuras, Hand of Ragnaros"){
//                continue;
                return;
            }

            if(name == "Aged Brie") {
                if (item.getQuality() < 50) {
                    IncreaseQuality(item);
                }
            } else if( name == "Backstage passes to a TAFKAL80ETC concert") {
                if (item.getQuality() < 50) {
                    IncreaseQuality(item);

                    if (item.getSellIn() < 11) {
                        IncreaseQuality(item);
                    }

                    if (item.getSellIn() < 6) {
                        IncreaseQuality(item);
                    }
                }
            } else{
                if (item.getQuality() > 0) {
                    DecreaseQuality(item);
                }
            }

            item.setSellIn(item.getSellIn() - 1);

            if (item.getSellIn() < 0) {
                if (name == "Aged Brie") {
                    if (item.getQuality() < 50) {
                        IncreaseQuality(item);
                    }
                }else if (name == "Backstage passes to a TAFKAL80ETC concert") {
                    item.setQuality(0);
                }else {
                    if (item.getQuality() > 0) {
                        DecreaseQuality(item);
                    }
                }
            }
//        }
    }

    private void DecreaseQuality(Item item) {
        item.setQuality(item.getQuality() - 1);
    }

    private void IncreaseQuality(Item item) {
        item.setQuality(item.getQuality() + 1);
    }
}
