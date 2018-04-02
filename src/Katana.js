export class Katana {
    constructor(midiDevice) {
      this.midiDevice = midiDevice;
      midiDevice.onReceive(this.onReceive);
    }
    async init() {
        await this.set([0x7F, 0x00, 0x00, 0x01], [0x01]);
    }
    onReceive = (data) => {
        console.log(data);
    }

    async send(addr, command, data = []) {
      let rawData = [...addr, ...data];
      let sysexData = [0xF0, 0x41, 0x00, 0x00, 0x00, 0x00, 0x33, command, ...rawData, this.calculateChecksum(rawData), 0xF7];
      console.log(sysexData);
      await this.midiDevice.send(sysexData);
    }

    async query(addr, size = 1) {
      await this.send(addr, 0x11, [0x00, 0x00, 0x00, size]);
    }

    async set(addr, data) {
      console.log(`${addr}, ${data}`);
      await this.send(addr, 0x12, data);
    }

    calculateChecksum(data) {
      let acc = 0;
      for(let i = 0; i < data.length; i++) {
        acc = (acc + data[i]) & 0x7F;
      }
      return (128 - acc) & 0x7F;
    }
  }