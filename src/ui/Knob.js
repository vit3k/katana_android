
import React, { Component } from 'react';
import { View, FlatList, TouchableOpacity, Text, StyleSheet } from 'react-native';
import {ART} from 'react-native';
import Arc from './Arc';

function degreesToRadians(degrees) {
    return degrees * Math.PI/180;
}
export default class Knob extends React.Component {
    render() {
      const {minValue, maxValue, value, title, ...rest} = this.props;
      const radius = 50;
      let degree = 40 + 280 * (value - minValue)/(Math.abs(minValue) + Math.abs(maxValue));
      return (
        <View height={radius * 2 + 30}>
            <View width={radius * 2} height={radius * 2} style={{transform: [{rotate: '180deg'}]}}>
                <ART.Surface width={radius * 2} height={radius * 2} >
                    <Arc radius={radius}
                        startAngle={degreesToRadians(40)}
                        endAngle={degreesToRadians(320)}
                        stroke={'#E0E0E0'}
                        strokeWidth={10}/>

                    <Arc radius={radius}
                        startAngle={degreesToRadians(40)}
                        endAngle={degreesToRadians(degree)}
                        stroke={'#039BE5'}
                        strokeWidth={10}/>
                </ART.Surface>
            </View>
            <Text style={{color: '#039BE5', fontWeight: 'bold', fontSize: 20, textAlign: 'center', width: radius*2, position: 'absolute', top: 30}}>{value}</Text>
            <Text style={{color: '#757575', fontWeight: 'bold', fontSize: 20, textAlign: 'center', width: radius*2, position: 'absolute', top: 85}}>{title}</Text>
        </View>)
    }
}