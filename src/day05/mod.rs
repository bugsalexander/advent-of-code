use crate::day02::parse;
mod tests;

// state is composed of
// - instruction pointer: marks the _next_ instruction to execute
// - registers: a bunch of registers, each with a numeric value

// the whole shebang
pub fn parse_and_compute_zero(input: &str) -> usize {
    let mut vec = parse(input);

    let result = intcompute(&mut vec);

    match result.get(0) {
        Some(result) => *result,
        None => panic!("there was no zero index"),
    }
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
    let num1 = try_index_twice(vec, *index + 1);
    let num2 = try_index_twice(vec, *index + 2);
    let dest = try_index_twice(vec, *index + 3);

    vec[dest] = op(num1, num2);

    *index += 4;
}

/// indexes the vec. unwraps once.
fn try_index_once(vec: &[usize], index: usize) -> usize {
    if let Some(result) = vec.get(index) {
        return *result;
    }

    panic!("attempted to retrieve an invalid index");
}

/// indexes the vec, and then indexes again with the result. unwraps twice.
fn try_index_twice(vec: &[usize], index: usize) -> usize {
    return try_index_once(vec, try_index_once(vec, index));
}

// compute a unary operation
pub fn compute_unop<F>(vec: &mut Vec<usize>, index: &mut usize, op: F)
where
    F: Fn(&mut Vec<usize>, usize) -> (),
{
    if let Some(&target_index) = vec.get(*index + 1) {
        // now that we have our target index, call the operation
        op(vec, target_index);
    } else {
        panic!("attempted to retrieve an invalid index");
    }
}
