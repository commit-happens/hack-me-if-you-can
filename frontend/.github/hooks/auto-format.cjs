#!/usr/bin/env node
const fs = require("fs");
const path = require("path");
const { spawnSync } = require("child_process");

function readStdin() {
  try {
    return fs.readFileSync(0, "utf8");
  } catch {
    return "";
  }
}

function pad2(value) {
  return String(value).padStart(2, "0");
}

function formatTimestamp(date) {
  const yyyy = date.getFullYear();
  const mm = pad2(date.getMonth() + 1);
  const dd = pad2(date.getDate());
  const hh = pad2(date.getHours());
  const mi = pad2(date.getMinutes());
  const ss = pad2(date.getSeconds());
  return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`;
}

function isInside(parentDir, childPath) {
  const relative = path.relative(parentDir, childPath);
  return relative && !relative.startsWith("..") && !path.isAbsolute(relative);
}

const rawInput = readStdin().trim();
if (!rawInput) {
  process.exit(0);
}

let payload;
try {
  payload = JSON.parse(rawInput);
} catch {
  process.exit(0);
}

const filePath = payload?.tool_input?.file_path;
if (!filePath || typeof filePath !== "string") {
  process.exit(0);
}

const frontendDir = path.resolve(__dirname, "..", "..");
const candidatePath =
  path.isAbsolute(filePath) ? filePath : path.resolve(frontendDir, filePath);

if (!fs.existsSync(candidatePath) || !fs.statSync(candidatePath).isFile()) {
  process.exit(0);
}

const resolvedFilePath = fs.realpathSync(candidatePath);
if (!isInside(frontendDir, resolvedFilePath)) {
  process.exit(0);
}

const relativePath = path.relative(frontendDir, resolvedFilePath);
if (!relativePath) {
  process.exit(0);
}

spawnSync("npx", ["prettier", "--write", relativePath], {
  cwd: frontendDir,
  stdio: "ignore",
});

spawnSync("npx", ["eslint", "--fix", "--", relativePath], {
  cwd: frontendDir,
  stdio: "ignore",
});

const logPath = path.join(__dirname, "hook.log");
const logLine = `${formatTimestamp(new Date())} formatted ${resolvedFilePath}${path.sep === "\\" ? "\r\n" : "\n"}`;
fs.appendFileSync(logPath, logLine, "utf8");

process.exit(0);
