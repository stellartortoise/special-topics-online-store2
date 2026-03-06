package com.nscc.onlinestore2.repository;

import com.nscc.onlinestore2.entity.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
}
