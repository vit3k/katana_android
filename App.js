/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import Ble from './Ble';
import Usb from './Usb';

export default class App extends Component {
  render() {
    //return <Ble />
    return <Usb />
  }
}
