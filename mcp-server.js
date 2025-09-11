#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

// MCP Server with proper initialization
const projectPath = process.env.PROJECT_PATH || '/home/sahal/my/ijaa-frontend';

let requestId = 1;

function listDirectory(dirPath = '') {
    try {
        const fullPath = path.join(projectPath, dirPath);
        const items = fs.readdirSync(fullPath, { withFileTypes: true });
        return items.map(item => ({
            name: item.name,
            type: item.isDirectory() ? 'directory' : 'file',
            path: path.join(dirPath, item.name)
        }));
    } catch (error) {
        throw new Error(`Failed to list directory: ${error.message}`);
    }
}

function readFile(filePath) {
    try {
        const fullPath = path.join(projectPath, filePath);
        const content = fs.readFileSync(fullPath, 'utf8');
        return { content, path: filePath };
    } catch (error) {
        throw new Error(`Failed to read file: ${error.message}`);
    }
}

function searchFiles(pattern, fileType = null) {
    try {
        const results = [];
        const searchDir = (dir) => {
            const items = fs.readdirSync(path.join(projectPath, dir), { withFileTypes: true });
            for (const item of items) {
                const itemPath = path.join(dir, item.name);
                if (item.isDirectory()) {
                    searchDir(itemPath);
                } else if (item.isFile()) {
                    if (pattern && !item.name.toLowerCase().includes(pattern.toLowerCase())) continue;
                    if (fileType && !item.name.endsWith('.' + fileType)) continue;
                    results.push({ name: item.name, path: itemPath });
                }
            }
        };
        searchDir('');
        return results;
    } catch (error) {
        throw new Error(`Failed to search files: ${error.message}`);
    }
}

function sendResponse(id, result = null, error = null) {
    const response = {
        jsonrpc: "2.0",
        id: id
    };
    
    if (error) {
        response.error = error;
    } else {
        response.result = result;
    }
    
    console.log(JSON.stringify(response));
}

// Handle MCP requests
process.stdin.on('data', (data) => {
    try {
        const lines = data.toString().trim().split('\n');
        for (const line of lines) {
            if (line.trim()) {
                const request = JSON.parse(line);
                const { method, params, id } = request;
                
                try {
                    let result;
                    switch (method) {
                        case 'initialize':
                            result = {
                                protocolVersion: "2024-11-05",
                                capabilities: {
                                    tools: {
                                        list_directory: {
                                            description: "List directory contents"
                                        },
                                        read_file: {
                                            description: "Read file contents"
                                        },
                                        search_files: {
                                            description: "Search for files"
                                        }
                                    }
                                },
                                serverInfo: {
                                    name: "pubmed-service-mcp",
                                    version: "1.0.0"
                                }
                            };
                            break;
                        case 'list_directory':
                            result = listDirectory(params?.path || '');
                            break;
                        case 'read_file':
                            result = readFile(params?.file_path || '');
                            break;
                        case 'search_files':
                            result = searchFiles(params?.pattern || '', params?.file_type);
                            break;
                        default:
                            throw new Error(`Unknown method: ${method}`);
                    }
                    
                    sendResponse(id, result);
                } catch (error) {
                    sendResponse(id, null, {
                        code: -32603,
                        message: error.message
                    });
                }
            }
        }
    } catch (error) {
        sendResponse(null, null, {
            code: -32700,
            message: `Parse error: ${error.message}`
        });
    }
});

// Handle process termination
process.on('SIGINT', () => process.exit(0));
process.on('SIGTERM', () => process.exit(0));

