import React, { Component } from 'react';
import { View, Text, Slider, StyleSheet, Picker, Switch } from 'react-native';
import Knob from './Knob';
import _ from 'lodash';

export default class SingleEffect extends Component {
    constructor(props) {
        super(props);
        this.state = {
            type: 0x00
        }
        this.throttleParamUpdate = _.throttle(this.paramUpdate, 500);
    }
    send = async (item) => {
        await this.props.katana.set([0x60, 0x00, 0x12, 0x14], [1]);
        //await katana.query([0x60, 0x00, 0x12, 0x14]);
    }
    randValue(min, max) {
        return Math.round(Math.random() * (Math.abs(min) + Math.abs(max)) + min);
    }
    setType = (type) => {
        this.setState({
            type
        });
    }
    paramUpdate = async (offset, value) => {
        console.log(`${offset} ${value}`);
        //await this.props.katana.set()
    }
    render() {
        return (
        <View style={styles.container}>
            <Switch value={true}/>
        {this.props.def.types ?
            <View style={styles.picker}>
                <Picker style={{flex: 1}}
                        onValueChange={this.setType}
                        selectedValue={this.state.type}>
                    {this.props.def.types.map( type =>
                        <Picker.Item label={type.name}
                                     value={type.value}
                                     key={type.value} />)}
                </Picker>
            </View>
        : null}
            <View style={styles.knobs}>
                {this.props.def.params.map(param =>
                    <Knob minValue={param.minValue}
                          maxValue={param.maxValue}
                          value={this.randValue(param.minValue, param.maxValue)}
                          title={param.name}
                          key={param.offset}
                          onValueChanged={(value) => this.throttleParamUpdate(param.offset, value)}/>)}
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