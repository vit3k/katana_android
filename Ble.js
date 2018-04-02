/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  NativeEventEmitter,
  PermissionsAndroid,
  FlatList
} from 'react-native';
import {NativeModules} from 'react-native';
import BleManager from 'react-native-ble-manager';
import _ from 'lodash';
import { Midi, MidiDevice } from './Midi';
import { Katana } from './Katana';

const BleManagerModule = NativeModules.BleManager;
const bleManagerEmitter = new NativeEventEmitter(BleManagerModule);

const MIDI_SERVICE_UID = '03B80E5A-EDE8-4B33-A751-6CE34EC4C700'.toLowerCase();
const MIDI_IO_CHARACTERISTIC_UID = '7772E5DB-3868-4112-A1A9-F2669D106BF3'.toLowerCase();

export default class Ble extends Component {
  state = {
    devices: new Map(),
    scanning: false,
    currentDevice: null
  }
  midi = new Midi()
  componentDidMount = async () => {
    await BleManager.start();
    this.handlerDiscover = bleManagerEmitter.addListener('BleManagerDiscoverPeripheral', this.handleDiscoverPeripheral );
    this.handlerStop = bleManagerEmitter.addListener('BleManagerStopScan', this.handleStopScan );
    if (Platform.OS === 'android' && Platform.Version >= 23) {
      let result = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION);
      if (result) {
        console.log("Permission is OK");
      } else {
        let requestResult = await PermissionsAndroid.requestPermission(PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION);
        if (requestResult) {
          console.log("User accept");
        } else {
          console.log("User refuse");
        }
      }
      try {
        await BleManager.enableBluetooth();
      } catch(e) {
        console.log('BT disabled');
      }
    }
    await this.getDevices();
  }

  handleMidiData = (data) => {
    console.log(data);
  }
  handleDiscoverPeripheral = async (device) => {
    if (!this.state.devices.has(device.id)) {
      let devices = _(this.state.devices).cloneDeep();
      devices.set(device.id, device);
      this.setState({
        devices
      });
    }
  }

  handleStopScan = async () => {
    console.log('scan stopped');
    this.setState({
      scanning: false
    });

    this.connect(this.state.devices.values().next().value);
  }
  connect = async (device) => {
    await BleManager.connect(device.id);
    let midiDevice = await this.midi.openBluetoothDevice(device.id);
    console.log(midiDevice);
    let katana = new Katana(midiDevice);
    await katana.init();
    console.log(katana);
    this.setState({
      currentDevice: katana
    })
  }
  getDevices = async () => {
    this.setState({
      scanning: true,
      currentDevice: null
    });
    await BleManager.scan([MIDI_SERVICE_UID], 3, true);
    console.log('scan started');
  }
  send = async () => {
    //this.state.currentDevice.set([0x01, 0x00, 0xF0, 0x01], [0x76]);
    await this.state.currentDevice.set([0x7F, 0x00, 0x00, 0x01], 0x01);
  }
  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={this.getDevices}>
        <Text style={styles.welcome}>
          Scan
        </Text>
        </TouchableOpacity>
        { this.state.currentDevice === null ?
        <FlatList
          keyExtractor={ item => item.id}
          renderItem={({item}) =>
            <Text onPress={() => this.connect(item)}>{item.name}</Text>}
          data={[...this.state.devices.values()]}
          refreshing={this.state.scanning}
          onRefresh={this.getDevices}
        /> : <View>
          <TouchableOpacity onPress={this.send}>
            <Text style={styles.welcome}>Send</Text>
          </TouchableOpacity>
        </View>}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  }
});
