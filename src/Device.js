import React, { Component } from 'react';
import { View, Text, Slider, StyleSheet, Picker } from 'react-native';
import SingleEffect from './ui/SingleEffect';
import { Booster } from './katana/Effects';

export default class Device extends Component {
    componentDidMount = () => {

    }
    send = async (item) => {
        await this.props.katana.set([0x60, 0x00, 0x12, 0x14], [1]);
        //await katana.query([0x60, 0x00, 0x12, 0x14]);
    }
    render() {
        return (
        <View style={styles.container}>
            <SingleEffect def={Booster} values={null}/>
        </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#EEEEEE',
        flex: 1
    }
});