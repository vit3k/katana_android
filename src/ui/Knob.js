
import React, { Component } from 'react';
import { View, FlatList, TouchableOpacity, Text, StyleSheet, PanResponder, Animated } from 'react-native';
import {ART} from 'react-native';
import Arc2 from './Arc2';
import AnimatedArc2 from './AnimatedArc2';

function degreesToRadians(degrees) {
    return degrees * Math.PI/180;
}
export default class Knob extends React.Component {
    state = {
        value: 0,
        startingValue: 0
    }
    updateValue(dy) {
        let value = this.state.startingValue;
        value -= Math.round(dy/2);
        if (value < this.props.minValue) {
            value = this.props.minValue;
        }
        if (value > this.props.maxValue) {
            value = this.props.maxValue;
        }
        this.setState({value});
        if(typeof(this.props.onValueChanged) === 'function') {
            this.props.onValueChanged(value);
        }
    }
    componentWillMount = () => {
        this._panResponder = PanResponder.create({
          // Ask to be the responder:
          onStartShouldSetPanResponder: (evt, gestureState) => true,
          onStartShouldSetPanResponderCapture: (evt, gestureState) => true,
          onMoveShouldSetPanResponder: (evt, gestureState) => true,
          onMoveShouldSetPanResponderCapture: (evt, gestureState) => true,

          onPanResponderGrant: (evt, gestureState) => {
            this.setState({startingValue: this.state.value});
          },
          onPanResponderMove: (evt, gestureState) => {
            this.updateValue(gestureState.dy);
          },
          onPanResponderTerminationRequest: (evt, gestureState) => true,
          onPanResponderRelease: (evt, gestureState) => {
            this.updateValue(gestureState.dy);
          },
          onPanResponderTerminate: (evt, gestureState) => {
          },
          onShouldBlockNativeResponder: (evt, gestureState) => {
            return true;
          },
        });
      }
      componentDidMount() {
          this.setState({value: this.props.value});
      }
    render() {
      const {minValue, maxValue, value, title, ...rest} = this.props;
      let fill = (this.state.value - minValue)/(Math.abs(minValue) + Math.abs(maxValue)) * 100;
      return (
        <View {...this._panResponder.panHandlers} height={100 + 30}>
                <Arc2
                size={100}
                width={10}
                fill={fill}
                tintColor="#039BE5"
                backgroundColor="#E0E0E0"
                arcSweepAngle={270}
                rotation={225}
                />

            <Text style={{color: '#039BE5', fontWeight: 'bold', fontSize: 20, textAlign: 'center', width: 100, position: 'absolute', top: 30}}>{this.state.value}</Text>
            <Text style={{color: '#757575', fontWeight: 'bold', fontSize: 20, textAlign: 'center', width: 100, position: 'absolute', top: 85}}>{title}</Text>
        </View>)
    }
}