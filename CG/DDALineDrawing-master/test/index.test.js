const expect = require('chai').expect;
const index = require('../src/index');

describe('DDALine', () => {
    it('should return correct array - case1', () => {
        const res = [ { x: 2, y: 2 },
            { x: 3, y: 2 },
            { x: 4, y: 1 },
            { x: 5, y: 1 },
            { x: 6, y: 0 },
            { x: 7, y: 0 },
            { x: 8, y: 0 },
            { x: 9, y: -1 },
            { x: 10, y: -1 },
            { x: 11, y: -2 },
            { x: 12, y: -2 } ]
      
        expect(index.DDALine(2,2,12,-2)).to.deep.equal(res);   
    });

    it('should return correct array - case2', () => {
        const res = [ { x: 2, y: 3 },
            { x: 2, y: 4 },
            { x: 3, y: 5 },
            { x: 3, y: 6 },
            { x: 4, y: 7 },
            { x: 4, y: 8 },
            { x: 5, y: 9 },
            { x: 5, y: 10 } ]
      
        expect(index.DDALine(2,3,5,10)).to.deep.equal(res);   
    });

    it('should return correct array - Horizontal', () => {
        const res = [ { x: 2, y: 3 },
            { x: 3, y: 3 },
            { x: 4, y: 3 },
            { x: 5, y: 3 },
            { x: 6, y: 3 },
            { x: 7, y: 3 },
            { x: 8, y: 3 } ]
      
        expect(index.DDALine(2,3,8,3)).to.deep.equal(res);   
    });

    
    it('should return correct array - Vertical', () => {
        const res = [ { x: 2, y: 3 },
            { x: 2, y: 4 },
            { x: 2, y: 5 },
            { x: 2, y: 6 },
            { x: 2, y: 7 },
            { x: 2, y: 8 } ]
      
        expect(index.DDALine(2,3,2,8)).to.deep.equal(res);   
    });

    it('should return correct array - Diagonal', () => {
        const res = [ { x: 2, y: 3 },
            { x: 3, y: 4 },
            { x: 4, y: 5 },
            { x: 5, y: 6 },
            { x: 6, y: 7 },
            { x: 7, y: 8 } ]
      
        expect(index.DDALine(2,3,7,8)).to.deep.equal(res);   
    });
});
