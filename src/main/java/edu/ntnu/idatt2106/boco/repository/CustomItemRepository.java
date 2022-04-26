package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;

import java.util.List;

public interface CustomItemRepository
{
    List<Item> search(String text);
}
