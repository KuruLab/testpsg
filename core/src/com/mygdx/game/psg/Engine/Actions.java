package com.mygdx.game.psg.Engine;


import com.mygdx.game.psg.Sprites.Cell;

import java.util.ArrayList;

public class Actions {


    public static ArrayList<Attribute.AttributeType>
            neutral = new ArrayList<Attribute.AttributeType>(),
            player = new ArrayList<Attribute.AttributeType>(),
            bot1 = new ArrayList<Attribute.AttributeType>(),
            bot2 = new ArrayList<Attribute.AttributeType>(),
            bot3 = new ArrayList<Attribute.AttributeType>(),
            bot4 = new ArrayList<Attribute.AttributeType>(),
            bot5 = new ArrayList<Attribute.AttributeType>();


    public void AddAction(Cell.Team team, Attribute.AttributeType attributeType){
        switch (team){
            case NEUTRAL: neutral.add(attributeType);
            case PLAYER: player.add(attributeType);
            case BOT1: bot1.add(attributeType);
            case BOT2: bot2.add(attributeType);
            case BOT3: bot3.add(attributeType);
            case BOT4: bot4.add(attributeType);
            case BOT5: bot5.add(attributeType);
        }
    }

    public float FitnessCell(Attribute attribute, Cell.Team team){

        float actions;
        float attributes;

        for(int i=1;i <= 20; i++){

            attributes = attribute.AttributeCount(Attribute.AttributeType.values()[i]);

            switch (team){
                case NEUTRAL: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], neutral); return actions/neutral.toArray().length * attributes;
                case PLAYER: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], player);return actions/player.toArray().length * attributes;
                case BOT1: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], bot1);return actions/bot1.toArray().length * attributes;
                case BOT2: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], bot2);return actions/bot2.toArray().length * attributes;
                case BOT3: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], bot3);return actions/bot3.toArray().length * attributes;
                case BOT4: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], bot4);return actions/bot4.toArray().length * attributes;
                case BOT5: actions = attribute.AttributeCount(Attribute.AttributeType.values()[i], bot5);return actions/bot5.toArray().length * attributes;
            }
        }
        return 0;
    }
}
