package com.masterPi.data.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextJPA extends JpaRepository<Text, Long>{
    Text findFirstBySelectedEquals(Boolean Selected);
    List<Text> findAllByOrderByTimeStampDesc();
}
