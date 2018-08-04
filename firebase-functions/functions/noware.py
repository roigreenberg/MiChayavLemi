from sys import stdout as s_out
from hexdump import hexdump
import socket, sys
from thread import start_new_thread

class machine():
    def __init__(self):
        self.stack = [1234, 5678, 943]
        self.reg = [0] * 8
        self.flags = [False] * 6
        self.code = [0x0207002d, 0x02020003, 0x09050500, 0x01040700, 0x05040500, 0x04060400,
                     0x14060000, 0x03040600, 0x14050000, 0x0b050200, 0x10000003]
        self.mem = [0xcc] * 32 + map(ord, "I'll do what xnt tell me") + [0x41, 0x72, 0x6b, 0x43, 0x6f, 0x6e, 0x7b, 0x2a,
                                                                         0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a,
                                                                         0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a,
                                                                         0x7d] + [0xcc] * 175
        self.ip = 0
        self.opcodes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}

    def execute(self, conn):
        pass

    def get_state(self):
        output = ''
        output += "\n\nRegisters:\n"
        output += "-" * 120 + "\n"
        for i in range(0, len(self.reg), 2):
            output += "r%02d=0x%08x\tr%02d=0x%08x\n" % (i, self.reg[i], i + 1, self.reg[i + 1])

        output += "\n\nStack:\n"
        output += "-" * 120 + "\n"
        output += hexdump(''.join(map(lambda x: chr(int(x, 16)), [item for sublist in
                                                                  [[a[i:i + 2] for i in range(0, len(a), 2)] for
                                                                   a in map(lambda x: hex(x)[2:], self.stack)]
                                                                  for item in sublist])), 'return')

        output += "\n\nMemory:\n"
        output += "-" * 120 + "\n"
        output += hexdump(''.join(map(lambda x: chr(int(x, 16)), [item for sublist in
                                                                  [[a[i:i + 2] for i in range(0, len(a), 2)] for
                                                                   a in
                                                                   map(lambda x: hex(x)[2:], self.mem[:32])] for
                                                                  item in sublist])), 'return')

        output += "\n\nOutput:\n"
        output += "-" * 120 + "\n"
        output += ''.join(map(chr, self.mem[32:56])) + '\n'
        return output


m = machine()
print(m.code)
print(m.get_state())