package com.katas.gildedrose;

import junit.framework.TestCase;

public class OracleTests extends TestCase {

    // exact copy of existing (legacy) code
    class Oracle {

        private Item[] items;
        public Item[] getItems() { return items; }

        public Oracle() {
            items = new Item[] {
                    new Item("+5 Dexterity Vest", 10, 20),
                    new Item("Aged Brie", 2, 0),
                    new Item("Elixir of the Mongoose", 5, 7),
                    new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                    new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                    new Item("Conjured Mana Cake", 3, 6)
            };
        }

        public void updateQuality() {
            for (int i = 0; i < items.length; i++) {
                if (items[i].Name != "Aged Brie"
                        && items[i].Name != "Backstage passes to a TAFKAL80ETC concert") {
                    if (items[i].getQuality() > 0) {
                        if (items[i].Name != "Sulfuras, Hand of Ragnaros") {
                            items[i].setQuality(items[i].getQuality() - 1);
                        }
                    }
                } else {
                    if (items[i].getQuality() < 50) {
                        items[i].setQuality(items[i].getQuality() + 1);

                        if (items[i].Name == "Backstage passes to a TAFKAL80ETC concert") {
                            if (items[i].getSellIn() < 11) {
                                if (items[i].getQuality() < 50) {
                                    items[i].setQuality(items[i].getQuality() + 1);
                                }
                            }

                            if (items[i].getSellIn() < 6) {
                                if (items[i].getQuality() < 50) {
                                    items[i].setQuality(items[i].getQuality() + 1);
                                }
                            }
                        }
                    }
                }

                if (items[i].Name != "Sulfuras, Hand of Ragnaros") {
                    items[i].setSellIn(items[i].getSellIn() - 1);
                }

                if (items[i].getSellIn() < 0) {
                    if (items[i].Name != "Aged Brie") {
                        if (items[i].Name != "Backstage passes to a TAFKAL80ETC concert") {
                            if (items[i].getQuality() > 0) {
                                if (items[i].Name != "Sulfuras, Hand of Ragnaros") {
                                    items[i].setQuality(items[i].getQuality() - 1);
                                }
                            }
                        } else {
                            items[i].setQuality(items[i].getQuality()
                                    - items[i].getQuality());
                        }
                    } else {
                        if (items[i].getQuality() < 50) {
                            items[i].setQuality(items[i].getQuality() + 1);
                        }
                    }
                }
            }
        }
    }

    public void test_OracleTest() {
        Oracle oracle = new Oracle();
        Store  store  = new Store();

        for (int day = 0; day < 20; ++day)
        {
            oracle.updateQuality();
            store.updateQuality();

            for (int i = 0; i < oracle.getItems().length; ++i)
            {
                assertEquals(oracle.getItems()[i].Name,         store.getItems()[i].Name);
                assertEquals(oracle.getItems()[i].getQuality(), store.getItems()[i].getQuality());
                assertEquals(oracle.getItems()[i].getSellIn(),  store.getItems()[i].getSellIn());
            }
        }
    }
}
