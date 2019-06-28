package com.emall.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Key {
    private String key;         //键的字符串

    private int expireSeconds;  //失效时间
}
