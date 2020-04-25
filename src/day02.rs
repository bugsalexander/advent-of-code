use std::iter::FromIterator;

// day 02 of advent of code 2019

// ––––––––––––––––––––––– notes ––––––––––––––––––––––––
// program is a list of comma-separated integers
// ex 1,2,3,4,5

// index 0 is an opcode
// one of: 1, 2, 99

// 1 - indicates add operation
// read numbers at [1, 2] and add them. leave result at [3]

// 2 - indicates multiply operation (same as add)

// 99 - indicates done

// once done with an opcode, step forward 4 positions

// what value is left at position 0?

// ----- functions

// the whole shebang
pub fn parse_and_compute_zero(input: &str) -> usize {
  let mut vec = parse(input);
  let result = intcompute(&mut vec);

  match result.get(0) {
    Some(result) => *result,
    None => panic!("there was no zero index"),
  }
}

// parse the form 0,1,2,3,3 into a Vec<usize>
fn parse(input: &str) -> Vec<usize> {
  let nums = input.split(",").map(|item|{
    // try and parse each item to usize
    match str::parse::<usize>(item) {
      Ok(n) => n,
      Err(e) => panic!(e),
    }
  });
  Vec::from_iter(nums)
}

// runs an int program
fn intcompute(regs: &mut Vec<usize>) -> Vec<usize> {
  
  let mut index: usize = 0;

  let plus = |n1,n2|{n1+n2};
  let times = |n1,n2|{n1*n2};
  
  loop {
    match regs.get(index) {
      Some(1) => compute_op(regs, index, plus),
      Some(2) => compute_op(regs, index, times),
      Some(99) => return regs.to_vec(),
      Some(_) => panic!("received unknown opcode"),
      None => panic!("expected instruction, but found none"),
    }
    
    // increment index by 4.
    index += 4;
  }
}

fn compute_op<F>(vec: &mut Vec<usize>, index: usize, op: F) where F: Fn(usize, usize) -> usize {
  
  match [vec.get(index + 1), vec.get(index + 2), vec.get(index + 3)] {
    [Some(index1), Some(index2), Some(put1)] => {
      let put_index = *put1;
      match [vec.get(*index1), vec.get(*index2)] {
        [Some(num1), Some(num2)] => vec[put_index] = op(*num1, *num2),
        _ => panic!("attempted to retrieve an invalid index"),
      }
    },
    _ => panic!("attempted to retrieve an invalid index"),
  }
}

#[cfg(test)]
mod tests {
  use super::*;
  
  #[test]
  fn test_parse() {
    assert_eq!(parse("1,2,3"), vec![1,2,3]);
  }
  
  #[test]
  fn test_compute() {
    assert_eq!(intcompute(&mut vec![1,0,0,0,99]), vec![2,0,0,0,99]);
    assert_eq!(intcompute(&mut vec![2,3,0,3,99]), vec![2,3,0,6,99]);
    assert_eq!(intcompute(&mut vec![2,4,4,5,99,0]), vec![2,4,4,5,99,9801]);
    assert_eq!(intcompute(&mut vec![1,1,1,4,99,5,6,0,99]), vec![30,1,1,4,2,5,6,0,99]);
    assert_eq!(intcompute(&mut vec![1,9,10,3,2,3,11,0,99,30,40,50]), vec![3500,9,10,70,2,3,11,0,99,30,40,50]);
  }
}