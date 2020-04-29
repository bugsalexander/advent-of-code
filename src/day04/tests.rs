#[cfg(test)]
use super::*;

#[test]
fn oracle_works() {
  assert_eq!(oracle("255", "555"), 30);
}

#[test]
fn part_two_works() {
  assert_eq!(oracle("123444", "123444"), 0);
  assert_eq!(oracle("111122", "111122"), 1);
}
