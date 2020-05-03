use std::iter::FromIterator;

// state is composed of
// - instruction pointer: marks the _next_ instruction to execute
// - registers: a bunch of registers, each with a numeric value

// the whole shebang
pub fn parse_and_compute_zero(input: &str, inp1: usize, inp2: usize) -> usize {
    let mut vec = parse(input);

    let result = intcompute(&mut vec);

    match result.get(0) {
        Some(result) => *result,
        None => panic!("there was no zero index"),
    }
}

// parse the form 0,1,2,3,3 into a Vec<usize>
pub fn parse(input: &str) -> Vec<usize> {
    let mut i = 0;
    let nums = input.trim().split(',').map(|item| {
        // try and parse each item to usize
        match str::parse::<usize>(item) {
            Ok(n) => {
                i += 1;
                return n;
            }
            Err(e) => panic!("value {} panicked at index {} with error {}", item, i, e),
        }
    });
    Vec::from_iter(nums)
}

// runs an int program
pub fn intcompute(regs: &mut Vec<usize>) -> Vec<usize> {
    let mut index: usize = 0;

    let plus = |n1, n2| n1 + n2;
    let times = |n1, n2| n1 * n2;

    loop {
        match regs.get(index) {
            Some(1) => compute_binop(regs, &mut index, plus),
            Some(2) => compute_binop(regs, &mut index, times),
            Some(99) => return regs.to_vec(),
            Some(_) => panic!("received unknown opcode"),
            None => panic!("expected instruction, but found none"),
        }
    }
}

// compute a binary operation
pub fn compute_binop<F>(vec: &mut Vec<usize>, index: &mut usize, op: F)
where
    F: Fn(usize, usize) -> usize,
{
    if let [Some(index1), Some(index2), Some(put1)] = [
        vec.get(*index + 1),
        vec.get(*index + 2),
        vec.get(*index + 3),
    ] {
        let put_index = *put1;
        if let [Some(num1), Some(num2)] = [vec.get(*index1), vec.get(*index2)] {
            vec[put_index] = op(*num1, *num2);
            // increment index by 4
            *index += 4;
        } else {
            panic!("attempted to retrieve an invalid index");
        }
    } else {
        panic!("attempted to retrieve an invalid index");
    }
}

// compute a unary operation
pub fn compute_unop<F>(vec: &mut Vec<usize>, index: &mut usize, op: F)
where
    F: Fn(&mut Vec<usize>, usize) -> (),
{
    if let Some(&target_index) = vec.get(*index + 1) {
        // now that we have our target index, call the operation
        op(&mut vec, target_index);
    }
}
