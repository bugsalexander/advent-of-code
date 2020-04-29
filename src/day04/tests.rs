#[cfg(test)]
use super::*;

#[test]
fn oracle_works() {
  assert_eq!(oracle("255", "555"), 30);
}
