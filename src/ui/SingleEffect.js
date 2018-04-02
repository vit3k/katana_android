import React, { Component } from 'react';
import { View, Text, Slider, StyleSheet, Picker } from 'react-native';
import Knob from './Knob';

export default class SingleEffect extends Component {
    componentWillMount = () => {

    }
    send = async (item) => {
        await this.props.katana.set([0x60, 0x00, 0x12, 0x14], [1]);
        //await katana.query([0x60, 0x00, 0x12, 0x14]);
    }
    randValue(min, max) {
        return Math.round(Math.random() * (Math.abs(min) + Math.abs(max)) + min);
    }
    render() {
        return (
        <View style={styles.container}>
            <View style={styles.picker}>
            <Picker style={{flex: 1}}>
                {this.props.def.types.map( type => <Picker.Item label={type.name} value={type.value} key={type.value} />)}
            </Picker>
        </View>
        <View style={styles.knobs}>
            {this.props.def.params.map(param => <Knob minValue={param.minValue} maxValue={param.maxValue} value={this.randValue(param.minValue, param.maxValue)} title={param.name} key={param.offset} />)}
        </View>
        </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#EEEEEE',
        flex: 1
    },
    knobs: {
        paddingLeft: 20,
        paddingRight: 20,
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'space-evenly',
        flexWrap: 'wrap'
    },
    picker: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignContent: 'center'
    }
});