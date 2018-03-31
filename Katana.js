export class Katana {
    constructor(midiDevice) {
      this.midiDevice = midiDevice;
      midiDevice.onReceive(this.onReceive);
      this.set([0x7F, 0x00, 0x00, 0x01], 0x01);
    }

    onReceive = (data) => {
        console.log(data);
    }

    send(addr, command, data = []) {
      let rawData = [...addr, ...data];
      //let sysexData = [0xF0, 0x41, 0x00, 0x00, 0x00, 0x00, 0x33, command, ...rawData, this.calculateChecksum(rawData), 0xF7];
      let sysexData = [0x00, 0x41, 0x00, 0x00, 0x00, 0x00, 0x33, command, ...rawData, this.calculateChecksum(rawData), 0x07];
      console.log(sysexData);
      this.midiDevice.send(sysexData);
    }

    query(addr) {
      return this.send(addr, 0x11);
    }

    set(addr, data) {
        console.log(`${addr}, ${data}`);
      return this.send(addr, 0x12, data);
    }

    calculateChecksum(data) {
      let acc = 0;
      for(let i = 0; i < data.length; i++) {
        acc = (acc + data[i]) & 0x7F;
      }
      return (128 - acc) & 0x7F;
    }
  }