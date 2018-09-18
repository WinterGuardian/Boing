var canvas;
var ctx;

var bodies;

$(function() {
    canvas = document.getElementById("testEngine");
    ctx = canvas.getContext("2d");

    init();
    setInterval(update, 16);
});

function init() {
    bodies = [{
        x: 50,
        y: 50,
        velX: 10,
        velY: 0,
        edges: [
            {
                x: 10,
                y: 0,
                length: 20,
                normalX: 1,
                normalY: 0
            },
            {
                x: -20,
                y: 0,
                length: 20,
                normalX: -1,
                normalY: 0
            },
            {
                x: -5,
                y: 10,
                length: 30,
                normalX: 0,
                normalY: 1
            },
            {
                x: -5,
                y: -10,
                length: 30,
                normalX: 0,
                normalY: -1
            }
        ],
        weight: 1
    },  {
        x: 300,
        y: 50,
        velX: -8,
        velY: 0,
        edges: [
            {
                x: -6,
                y: 0,
                length: 30,
                normalX: -1,
                normalY: 0
            },
            {
                x: 12,
                y: 0,
                length: 30,
                normalX: 1,
                normalY: 0
            },
            {
                x: 3,
                y: 15,
                length: 18,
                normalX: 0,
                normalY: 1
            },
            {
                x: 3,
                y: -15,
                length: 18,
                normalX: 0,
                normalY: -1
            }
        ],
        weight: 2
    }];
}

function update() {
    tick(1 / 2);
    render();
}

function tick(delta) {
    bodies = bodies.sort(function (a, b) {
        return b.weight - a.weight;
    });

    bodies.forEach(function(body) {
        move(body, body.velX * delta, body.velY * delta, body.weight)
    });
}

function move(bodyA, x, y, weight) {
    if(!bodies.some(function(bodyB) {
        if(bodyA == bodyB)
            return false;

        if(weight > bodyB.weight) {
            collision(bodyA, x, y, bodyB).forEach(function(coll) {
                move(bodyB, coll.penetration * coll.normalX, coll.penetration * coll.normalY, weight);
            });
            return false;
        } else {
            var collided = false;
            collision(bodyA, x, y, bodyB).forEach(function(coll) {
                collided = true;

                x -= coll.penetration * coll.normalX;
                y -= coll.penetration * coll.normalY;
            });
            if(!collided || x * y === 0)
                return false;

            move(bodyA, x, y, bodyB.weight);
            return true;
        }
    })) {
        bodyA.x += x;
        bodyA.y += y;
    }
}

function collision(bodyA, x, y, bodyB) {

    var collisions = [];

    bodyA.edges.forEach(function(edgeA) {
        bodyB.edges.forEach(function(edgeB) {
            if(edgeA.normalX != -edgeB.normalX || edgeA.normalY != -edgeB.normalY)
                return;

            var rAx = bodyA.x + edgeA.x;
            var rAy = bodyA.y + edgeA.y;
            var rBx = bodyB.x + edgeB.x;
            var rBy = bodyB.y + edgeB.y;

            if(rAx * edgeA.normalX > rBx * edgeA.normalX
            || rAy * edgeA.normalY > rBy * edgeA.normalY)
                return;

            var pene = dot(rAx - rBx + x, rAy - rBy + y, edgeA.normalX, edgeA.normalY);

            if(pene <= 0)
                return;

            var t = dot(rAx - rBx, rAy - rBy, edgeA.normalX, edgeA.normalY) /
                dot(-x, -y, edgeA.normalX, edgeA.normalY);

            var cAx = rAx + t * x;
            var cAy = rAy + t * y;

            if((contactSurface(cAx, cAy, edgeA.length, rBx, rBy, edgeB.length, edgeA.normalX, edgeA.normalY)) <= 0)
                return;

            collisions.push({
                penetration: pene,
                normalX: edgeA.normalX,
                normalY: edgeA.normalY
            });
        });
    });

    return collisions;
}

function render() {
	ctx.clearRect(0, 0, canvas.width, canvas.height);

    bodies.forEach(function(body) {
        ctx.beginPath();
        ctx.rect(body.x - 2, body.y - 2, 4, 4);
        ctx.fillStyle = "#50FF50";
        ctx.fill();
        ctx.closePath();

        body.edges.forEach(function(edge){
            ctx.beginPath();
            ctx.rect(body.x + edge.x - edge.length * Math.abs(edge.normalY) / 2,
                body.y + edge.y - edge.length * Math.abs(edge.normalX) / 2,
                Math.max(2, edge.length * Math.abs(edge.normalY)),
                Math.max(2, edge.length * Math.abs(edge.normalX)));

            ctx.fillStyle = "#FF5050";
            ctx.fill();
            ctx.closePath();
        })
    })
}

function contactSurface(ax, ay, sizeA, bx, by, sizeB, nx, ny) {
    sizeA /= 2;
    sizeB /= 2;

    //we take the 2 extremities of the 2 limits
    var limitA1 = ny * (ax + sizeA) + nx * (ay + sizeA);
    var limitA2 = ny * (ax - sizeA) + nx * (ay - sizeA);
    var limitB1 = ny * (bx + sizeB) + nx * (by + sizeB);
    var limitB2 = ny * (bx - sizeB) + nx * (by - sizeB);

    //yields the overlapping length
    return Math.min(Math.max(limitA1, limitA2), Math.max(limitB1, limitB2)) //minimum of the maximums
        - Math.max(Math.min(limitA1, limitA2), Math.min(limitB1, limitB2)); //maximum of the minimums
}

function dot(x1, y1, x2, y2) {
    return x1 * x2 + y1 * y2;
}

function pow2(x) {
    return x * x;
}