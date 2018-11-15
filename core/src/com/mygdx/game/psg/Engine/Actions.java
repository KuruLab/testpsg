package com.mygdx.game.psg.Engine;


import com.mygdx.game.psg.Sprites.Cell;

import java.util.ArrayList;

public class Actions {


    public static ArrayList<Attribute.AttributeType> neutral, player, bot1, bot2, bot3, bot4, bot5;


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
        for(int i=1;i <= 20; i++){

            float actions;
            float attributes;

            attributes = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)));

            switch (team){
                case NEUTRAL: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), neutral);return actions/neutral.toArray().length * attributes;
                case PLAYER: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), player);return actions/player.toArray().length * attributes;
                case BOT1: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), bot1);return actions/bot1.toArray().length * attributes;
                case BOT2: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), bot2);return actions/bot2.toArray().length * attributes;
                case BOT3: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), bot3);return actions/bot3.toArray().length * attributes;
                case BOT4: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), bot4);return actions/bot4.toArray().length * attributes;
                case BOT5: actions = attribute.AttributeCount(Attribute.AttributeType.valueOf(Integer.toString(i)), bot5);return actions/bot5.toArray().length * attributes;
            }
        }
        return 0;
    }
}
