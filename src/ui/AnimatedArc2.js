// based on https://github.com/bgryszko/react-native-circular-progress

import React from 'react';
import PropTypes from 'prop-types';
import { View, ViewPropTypes, Platform, ART, AppState, Animated } from 'react-native';
const { Surface, Shape, Path, Group } = ART;
import Arc2 from './Arc2';

export default class AnimatedArc2 extends React.Component {
  constructor(props) {
      super(props);
    this.state = {
        fill: new Animated.Value(props.fill)
      };
  }
  componentWillReceiveProps(nextProps) {

      /*if(nextProps.fill !== this.props.fill) {
          Animated.timing(this.state.fill, {
              toValue: nextProps.fill,
              duration: 100
          }).start();
      }*/
  }
  render() {
    const { fill, ...other } = this.props;
    return (
      <Arc2
        {...other}
        fill={this.state.fill}
      />
    );
  }
}
