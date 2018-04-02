export const Booster = {
    baseAddr: [0x60, 0x00, 0x00, 0x30],
    paramsBaseAddr: [0x60, 0x00, 0x00, 0x32],
    params: [
        {
            name: 'Drive',
            minValue: 0,
            maxValue: 100,
            offset: 0x00
        },
        {
            name: 'Tone',
            minValue: -50,
            maxValue: 50,
            offset: 0x02
        },
        {
            name: 'Level',
            minValue: 0,
            maxValue: 100,
            offset: 0x05
        },
        {
            name: 'Bottom',
            minValue: -50,
            maxValue: 50,
            offset: 0x01
        },
        {
            name: 'Direct Mix',
            minValue: 0,
            maxValue: 100,
            offset: 0x06
        },
    ],
    types: [
        { name: 'Mid Boost', value: 0x00 },
        { name: 'Clean Boost', value: 0x01 },
        { name: 'Treble Boost', value: 0x02 },
        { name: 'Crunch OD', value: 0x03 },
        { name: 'Natural OD', value: 0x04 },
        { name: 'Warm OD', value: 0x05 },
        { name: 'Fat DS', value: 0x06 },
        { name: 'Metal DS', value: 0x08 },
        { name: 'Oct Fuzz', value: 0x09 },
        { name: 'Blues Drive', value: 0x0A },
        { name: 'Overdrive', value: 0x0B },
        { name: 'T-Scream', value: 0x0C },
        { name: 'Turbo OD', value: 0x0D },
        { name: 'Distortion', value: 0x0E },
        { name: 'Rat', value: 0x0F },
        { name: 'Guv DS', value: 0x10 },
        { name: 'DST+', value: 0x11 },
        { name: 'Metal Zone', value: 0x12 },
        { name: '\`60s Fuzz', value: 0x13 },
        { name: 'Muff Fuzz', value: 0x14 }
    ]
}

