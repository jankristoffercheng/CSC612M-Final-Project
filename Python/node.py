import numba

nodeType = numba.deferred_type()

spec = [
    ('parent', numba.optional(nodeType)),
    ('start', numba.int64),
    ('end', numba.int64),
    ('error', numba.int64),
    ('pattern', numba.int64),
    ('indices', numba.int64[:]),
]

@numba.jitclass(spec)
class ParallelNode:
    def __init__(self, indices):
        self.parent = None
        self.start = 0
        self.end = 0
        self.error = 0
        self.pattern = 0
        self.indices = indices
        
nodeType.define(ParallelNode.class_type.instance_type)

class StandardNode:
     def __init__(self):
        self.parent = None
        self.start = 0
        self.end = 0
        self.error = 0
        self.pattern = ''
        self.indices = []